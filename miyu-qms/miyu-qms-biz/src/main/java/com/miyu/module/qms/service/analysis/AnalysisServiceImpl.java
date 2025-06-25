package com.miyu.module.qms.service.analysis;

import cn.hutool.core.util.ObjUtil;
import cn.iocoder.yudao.framework.common.util.collection.MapUtils;
import cn.iocoder.yudao.module.system.api.user.AdminUserApi;
import cn.iocoder.yudao.module.system.api.user.dto.AdminUserRespDTO;
import com.miyu.module.pdm.api.processPlanDetail.ProcessPlanDetailApi;
import com.miyu.module.pdm.api.processPlanDetail.dto.ProcedureRespDTO;
import com.miyu.module.qms.controller.admin.analysis.vo.*;
import com.miyu.module.qms.dal.dataobject.inspectionsheetrecord.InspectionSheetRecordDO;
import com.miyu.module.qms.dal.dataobject.inspectionsheetscheme.InspectionSheetSchemeDO;
import com.miyu.module.qms.dal.dataobject.inspectionsheetschemematerial.InspectionSheetSchemeMaterialDO;
import com.miyu.module.qms.dal.dataobject.unqualifiedregistration.UnqualifiedRegistrationDO;
import com.miyu.module.qms.enums.InspectionSheetSchemeStatusEnum;
import com.miyu.module.qms.service.inspectionsheetrecord.InspectionSheetRecordService;
import com.miyu.module.qms.service.inspectionsheetscheme.InspectionSheetSchemeService;
import com.miyu.module.qms.service.inspectionsheetschemematerial.InspectionSheetSchemeMaterialService;
import com.miyu.module.qms.service.unqualifiedregistration.UnqualifiedRegistrationService;
import com.miyu.module.qms.utils.StringListUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.annotation.Validated;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Duration;
import java.util.*;

/**
 * 质量分析 Service 实现类
 *
 * @author zhp
 */
@Service
@Validated
public class AnalysisServiceImpl implements AnalysisService {
    @Resource
    private InspectionSheetSchemeService inspectionSheetSchemeService;
    @Resource
    private UnqualifiedRegistrationService unqualifiedRegistrationService;
    @Resource
    private InspectionSheetRecordService inspectionSheetRecordService;
    @Resource
    private InspectionSheetSchemeMaterialService inspectionSheetSchemeMaterialService;

    @Resource
    private AdminUserApi userApi;

    @Resource
    private ProcessPlanDetailApi processPlanDetailApi;

    @Override
    public AnalysisNumberResp getAnalysisNumber(AnalysisReqVO vo) {
        AnalysisNumberResp resp = new AnalysisNumberResp();

        List<InspectionSheetSchemeDO> sheetSchemeDOS = inspectionSheetSchemeService.getInspectionSheetSchemeAnalysis(vo, false);
        Integer checkNumber = 0;
        Integer qualifiedNumber = 0;
        if (!CollectionUtils.isEmpty(sheetSchemeDOS)) {

            for (InspectionSheetSchemeDO sheetSchemeDO : sheetSchemeDOS) {
                checkNumber += sheetSchemeDO.getQuantity();
                qualifiedNumber += sheetSchemeDO.getQualifiedQuantity();
            }

        }
        resp.setCheckNumber(checkNumber);
        resp.setUnqualifiedNumber(checkNumber - qualifiedNumber);
        resp.setQualifiedNumber(qualifiedNumber);

        return resp;
    }

    @Override
    public List<BatchAnalysisResp> getBatchAnalysis(AnalysisReqVO vo) {

        List<BatchAnalysisResp> resps = new ArrayList<>();
        //查询 指定时间内 生产检验单  自检单排除在外
        List<InspectionSheetSchemeDO> sheetSchemeDOS = inspectionSheetSchemeService.getInspectionSheetSchemes(vo);


        if (!CollectionUtils.isEmpty(sheetSchemeDOS)) {
            Map<String, Integer> numberMap = new HashMap<>();//批次总数量
            Map<String, Integer> AQLNumberMap = new HashMap<>();//同批次检验单的数量
            Map<String, BigDecimal> AQLNumberAllMap = new HashMap<>();//同批次检验单的合格率总和
            for (InspectionSheetSchemeDO inspectionSheetSchemeDO : sheetSchemeDOS) {
//                if (numberMap.get(inspectionSheetSchemeDO.getBatchNumber()) != null) {
//                    numberMap.put(inspectionSheetSchemeDO.getBatchNumber(), inspectionSheetSchemeDO.getInspectionQuantity().intValue()));
//                } else {
//                    numberMap.put(inspectionSheetSchemeDO.getBatchNumber(), inspectionSheetSchemeDO.getInspectionQuantity().intValue());
//                }

                // 批次内检验数量
                if(numberMap.get(inspectionSheetSchemeDO.getBatchNumber()) == null){
                    numberMap.put(inspectionSheetSchemeDO.getBatchNumber(), inspectionSheetSchemeDO.getInspectionQuantity().intValue());
                }
                else {
                    numberMap.put(inspectionSheetSchemeDO.getBatchNumber(), inspectionSheetSchemeDO.getInspectionQuantity().intValue() + numberMap.get(inspectionSheetSchemeDO.getBatchNumber()));
                }

                BigDecimal rate = new BigDecimal(0.00);
                if (inspectionSheetSchemeDO.getInspectionSheetType().intValue() == 1) {//如果是抽检  通过AQL计算 合格率


                    BigDecimal m = new BigDecimal(inspectionSheetSchemeDO.getQuantity() - inspectionSheetSchemeDO.getQualifiedQuantity());

                    BigDecimal s = inspectionSheetSchemeDO.getAcceptanceQualityLimit().divide(m, 2, RoundingMode.HALF_DOWN);
                    BigDecimal b = new BigDecimal(1).subtract(s);

                    rate = rate.add(b);

                    rate = rate.multiply(new BigDecimal(100));
                } else {//如果是全检  通过合格数量/检测数量  计算合格率
                    rate = rate.add(new BigDecimal(100 * inspectionSheetSchemeDO.getQualifiedQuantity() / inspectionSheetSchemeDO.getQuantity()));
                }
                if (AQLNumberAllMap.get(inspectionSheetSchemeDO.getBatchNumber()) != null) {//如果有该批次的
                    AQLNumberAllMap.put(inspectionSheetSchemeDO.getBatchNumber(), AQLNumberAllMap.get(inspectionSheetSchemeDO.getBatchNumber()).add(rate));
                    AQLNumberMap.put(inspectionSheetSchemeDO.getBatchNumber(), AQLNumberMap.get(inspectionSheetSchemeDO.getBatchNumber()) + 1);
                } else {
                    AQLNumberAllMap.put(inspectionSheetSchemeDO.getBatchNumber(), rate);
                    AQLNumberMap.put(inspectionSheetSchemeDO.getBatchNumber(), 1);
                }
            }
            for (Map.Entry<String, BigDecimal> entry : AQLNumberAllMap.entrySet()) {
                //AQLMap.put(entry.getKey(), (Object) entry.getValue().divide(new BigDecimal(AQLNumberMap.get(entry.getKey()))));
                BatchAnalysisResp resp = new BatchAnalysisResp();
                resp.setName(entry.getKey());
                resp.setBatchCount(numberMap.get(entry.getKey()));
                resp.setPassRate(entry.getValue().divide(BigDecimal.valueOf(AQLNumberMap.get(entry.getKey())), 2, RoundingMode.HALF_DOWN));
                resps.add(resp);
            }

        }

        return resps;
    }

    @Override
    public List<DefectiveAnalysisResp> getDefectives(AnalysisReqVO vo) {
        List<DefectiveAnalysisResp> resps = new ArrayList<>();
        //获取不合格品登记表
        List<UnqualifiedRegistrationDO> list = unqualifiedRegistrationService.getDefectives(vo);
        Map<String, Integer> map = new HashMap<>();
        Map<String, String> codeMap = new HashMap<>();
        if (!CollectionUtils.isEmpty(list)) {
            for (UnqualifiedRegistrationDO unqualifiedRegistrationDO : list) {
                String key = unqualifiedRegistrationDO.getDefectiveCode();
                codeMap.put(key, unqualifiedRegistrationDO.getName());
                if (map.get(key) == null) {
                    map.put(key, 1);
                } else {
                    map.put(key, 1 + map.get(key));
                }
            }

            for (Map.Entry<String, String> entry : codeMap.entrySet()) {
                DefectiveAnalysisResp resp = new DefectiveAnalysisResp();

                resp.setName(entry.getValue());
                resp.setValue(map.get(entry.getKey()));
                resps.add(resp);
            }


        }

        return resps;
    }

    @Override
    public List<ItemAnalysisResp> getItemAnalysis(AnalysisReqVO vo) {
        List<ItemAnalysisResp> resps = new ArrayList<>();
        //获取检验记录
        List<InspectionSheetRecordDO> list = inspectionSheetRecordService.getInspectionSheetRecords(vo);

        if (!CollectionUtils.isEmpty(list)) {
            //key   检测项目ID       value   检测项目名称
            Map<String, String> nameMap = new HashMap<>();
            Map<String, Integer> checkNumberMap = new HashMap<>();//检测数量
            Map<String, Integer> unqualifiedNumberMap = new HashMap<>();//不合格数量
            Integer totalNumber = 0;
            for (InspectionSheetRecordDO recordDO : list) {
                String itemId = recordDO.getInspectionSchemeItemId();
                nameMap.put(itemId, recordDO.getInspectionSchemeItemName());

                if (checkNumberMap.get(itemId) == null) {
                    checkNumberMap.put(itemId, 1);
                } else {
                    checkNumberMap.put(itemId, 1 + checkNumberMap.get(itemId));
                }

                // 检验结果 专检 > 自检 > 质检
                int inspectResult = ObjUtil.defaultIfNull(ObjUtil.defaultIfNull(recordDO.getSpecInspectionResult(), recordDO.getMutualInspectionResult()), recordDO.getInspectionResult());
                // 2 不合格
                if (inspectResult == 2) {
                    if (unqualifiedNumberMap.get(itemId) == null) {
                        unqualifiedNumberMap.put(itemId, 1);
                    } else {
                        unqualifiedNumberMap.put(itemId, 1 + unqualifiedNumberMap.get(itemId));
                    }
                }
                totalNumber = totalNumber + 1;
            }
            for (Map.Entry<String, String> entry : nameMap.entrySet()) {
                Integer checkNum = checkNumberMap.get(entry.getKey());
                Integer unqualifiedNumber = unqualifiedNumberMap.get(entry.getKey()) == null ? 0 : unqualifiedNumberMap.get(entry.getKey());
                ItemAnalysisResp resp = new ItemAnalysisResp();
                resp.setItemId(entry.getKey());
                resp.setName(entry.getValue());
                resp.setItemNumber(checkNum);
                resp.setUnqualifiedNumber(unqualifiedNumber);
                resp.setPassRates(new BigDecimal(unqualifiedNumber).divide(BigDecimal.valueOf(totalNumber), 2, RoundingMode.HALF_DOWN).multiply(new BigDecimal(100.00)));
                resps.add(resp);
            }


            Collections.sort(resps, new Comparator<ItemAnalysisResp>() {
                @Override
                public int compare(ItemAnalysisResp o1, ItemAnalysisResp o2) {

                    return o2.getUnqualifiedNumber().compareTo(o1.getUnqualifiedNumber());
                }
            });

        }

        return resps;
    }

    @Override
    public List<ProcessAnalysisResp> getProcessAnalysis(AnalysisReqVO vo) {

        List<ProcessAnalysisResp> resps = new ArrayList<>();

        //查询所有带工序的质检记录
        List<InspectionSheetSchemeDO> sheetSchemeDOS = inspectionSheetSchemeService.getInspectionSheetSchemeAnalysis(vo, true);

        Map<String, Integer> checkNumberMap = new HashMap<>();
        Map<String, Integer> unqualifiedNumberMap = new HashMap<>();

        if (!CollectionUtils.isEmpty(sheetSchemeDOS)) {
            for (InspectionSheetSchemeDO sheetSchemeDO : sheetSchemeDOS) {
                String processId = sheetSchemeDO.getProcessId();
                Integer unqualifiedNumber = sheetSchemeDO.getQuantity() - sheetSchemeDO.getQualifiedQuantity();
                if (checkNumberMap.get(processId) == null) {
                    checkNumberMap.put(processId, sheetSchemeDO.getQuantity());
                } else {
                    checkNumberMap.put(processId, sheetSchemeDO.getQuantity() + checkNumberMap.get(processId));
                }

                if (unqualifiedNumberMap.get(processId) == null) {

                    unqualifiedNumberMap.put(processId, unqualifiedNumber);
                } else {
                    unqualifiedNumberMap.put(processId, unqualifiedNumber + unqualifiedNumberMap.get(processId));
                }
            }
        }

        // 工序 id 集合
        List<String> processIds = new ArrayList<>(checkNumberMap.keySet());
        // 获取工序信息
        Map<String, ProcedureRespDTO> processMap = processPlanDetailApi.getProcedureMapByIds(processIds);

        for (Map.Entry<String, Integer> entry : checkNumberMap.entrySet()) {
            Integer unqualifiedNumber = unqualifiedNumberMap.get(entry.getKey()) == null ? 0 : unqualifiedNumberMap.get(entry.getKey());
            ProcessAnalysisResp resp = new ProcessAnalysisResp();
            MapUtils.findAndThen(processMap, entry.getKey(), a -> resp.setProcessName(a.getProcedureName()));
            resp.setUnqualifiedNumber(unqualifiedNumber);
            resp.setCheckNumber(entry.getValue());
            resp.setQualifiedNumber(entry.getValue() - unqualifiedNumber);
            resp.setRate(new BigDecimal(entry.getValue() - unqualifiedNumber).divide(new BigDecimal(entry.getValue()), 2, RoundingMode.HALF_DOWN).multiply(new BigDecimal(100.00)));
            resps.add(resp);
        }

        Collections.sort(resps, new Comparator<ProcessAnalysisResp>() {
            @Override
            public int compare(ProcessAnalysisResp o1, ProcessAnalysisResp o2) {

                return o1.getRate().compareTo(o2.getRate());
            }
        });


        return resps;
    }

    @Override
    public List<WorkerAnalysisResp> getWorkerAnalysis(AnalysisReqVO vo) {
        List<WorkerAnalysisResp> resps = new ArrayList<>();
        // 查生产和成品
        vo.setSchemeType(Arrays.asList(2, 3));
        // 查询所有带工序的质检记录
        List<InspectionSheetSchemeDO> sheetSchemeDOS = inspectionSheetSchemeService.getInspectionSheetSchemeAnalysisWorker(vo);
        if (!CollectionUtils.isEmpty(sheetSchemeDOS)) {
            // 姓名
            Map<String, String> workerMap = new HashMap<>();
            // 数量
            Map<String, Integer> quantityNumberMap = new HashMap<>();
            // 不合格数量
            Map<String, Integer> unqualifiedNumberMap = new HashMap<>();

            for (InspectionSheetSchemeDO inspectionSheetSchemeDO : sheetSchemeDOS) {
                String assignmentId;
                // 生产自检人不为空
                if(inspectionSheetSchemeDO.getAssignmentId() != null){
                    assignmentId = inspectionSheetSchemeDO.getAssignmentId();
                }else {
                    continue;
                }

                // 分配人员ID
                if(workerMap.get(assignmentId) == null){
                    workerMap.put(assignmentId, assignmentId);
                    // 数量
                    quantityNumberMap.put(assignmentId, inspectionSheetSchemeDO.getQuantity());
                    // 不合格
                    unqualifiedNumberMap.put(assignmentId, inspectionSheetSchemeDO.getQuantity() - inspectionSheetSchemeDO.getQualifiedQuantity());
                }
                else {
                    quantityNumberMap.put(assignmentId, inspectionSheetSchemeDO.getQuantity() + quantityNumberMap.get(assignmentId));
                    unqualifiedNumberMap.put(assignmentId, inspectionSheetSchemeDO.getQuantity() - inspectionSheetSchemeDO.getQualifiedQuantity() + unqualifiedNumberMap.get(assignmentId));
                }
            }

            // 人员 ID 集合
            List<Long> userIdList = StringListUtils.stringListToLongList(new ArrayList<>(workerMap.keySet()));

            if(userIdList.size() == 0){
                return Collections.emptyList();
            }

            Map<Long, AdminUserRespDTO> userMap = userApi.getUserMap(userIdList);

            for (Map.Entry<String, String> entry : workerMap.entrySet()) {
                Integer quantityNumber = quantityNumberMap.get(entry.getKey()) == null ? 0 : quantityNumberMap.get(entry.getKey());
                Integer unqualifiedNumber = unqualifiedNumberMap.get(entry.getKey()) == null ? 0 : unqualifiedNumberMap.get(entry.getKey());
                WorkerAnalysisResp resp = new WorkerAnalysisResp();
                MapUtils.findAndThen(quantityNumberMap, entry.getKey(), a -> resp.setQualifiedNumber(quantityNumberMap.get(entry.getKey())));
                MapUtils.findAndThen(unqualifiedNumberMap, entry.getKey(), a -> resp.setUnqualifiedNumber(unqualifiedNumberMap.get(entry.getKey())));
                MapUtils.findAndThen(userMap, Long.valueOf(entry.getKey()), a -> resp.setName(a.getNickname()));
                resp.setRate(new BigDecimal(quantityNumber - unqualifiedNumber).divide(new BigDecimal(quantityNumber), 2, RoundingMode.HALF_DOWN).multiply(new BigDecimal(100.00)));
                resps.add(resp);
            }

            Collections.sort(resps, new Comparator<WorkerAnalysisResp>() {
                @Override
                public int compare(WorkerAnalysisResp o1, WorkerAnalysisResp o2) {
                    return o1.getRate().compareTo(o2.getRate());
                }
            });
        }

        return resps;
    }

    /**
     * 工时统计
     * @param vo
     * @return
     */
    @Override
    public List<WorkerHoursAnalysisResp> getWorkerHoursAnalysis(AnalysisReqVO vo) {
        List<WorkerHoursAnalysisResp> resps = new ArrayList<>();
        // 查询所有带工序的质检记录
        List<InspectionSheetSchemeDO> sheetSchemeDOS = inspectionSheetSchemeService.getInspectionSheetSchemeAnalysisWorker(vo);
        if (!CollectionUtils.isEmpty(sheetSchemeDOS)) {
            // 姓名
            Map<String, String> workerMap = new HashMap<>();
            // 数量
            Map<String, Integer> quantityNumberMap = new HashMap<>();
            // 工时
            Map<String, Long> workerHoursMap = new HashMap<>();

            for (InspectionSheetSchemeDO inspectionSheetSchemeDO : sheetSchemeDOS) {
                String assignmentId;
                // 来料检验
                if(inspectionSheetSchemeDO.getSchemeType() == 1){
                    if(inspectionSheetSchemeDO.getAssignmentId() != null){
                        assignmentId = inspectionSheetSchemeDO.getAssignmentId();
                    }else {
                        continue;
                    }
                }
                // 生产检验 成品检验
                else {
                    if(inspectionSheetSchemeDO.getSpecAssignmentId() != null){
                        assignmentId = inspectionSheetSchemeDO.getSpecAssignmentId();
                    }else {
                        continue;
                    }
                }

                // 分配人员ID
                if(workerMap.get(assignmentId) == null){
                    workerMap.put(assignmentId, assignmentId);
                    // 数量
                    quantityNumberMap.put(assignmentId, inspectionSheetSchemeDO.getQuantity());
                    // 用时
                    long millis = Duration.between(inspectionSheetSchemeDO.getAssignmentDate(), inspectionSheetSchemeDO.getEndTime()).toMillis();
                    workerHoursMap.put(assignmentId, millis);
                }
                else {
                    quantityNumberMap.put(assignmentId, inspectionSheetSchemeDO.getQuantity() + quantityNumberMap.get(assignmentId));
                    long millis = Duration.between(inspectionSheetSchemeDO.getAssignmentDate(), inspectionSheetSchemeDO.getEndTime()).toMillis();
                    workerHoursMap.put(assignmentId, millis + workerHoursMap.get(assignmentId));
                }
            }

            // 人员 ID 集合
            List<Long> userIdList = StringListUtils.stringListToLongList(new ArrayList<>(workerMap.keySet()));

            if(userIdList.size() == 0){
                return Collections.emptyList();
            }

            Map<Long, AdminUserRespDTO> userMap = userApi.getUserMap(userIdList);

            for (Map.Entry<String, String> entry : workerMap.entrySet()) {
                WorkerHoursAnalysisResp resp = new WorkerHoursAnalysisResp();
                MapUtils.findAndThen(quantityNumberMap, entry.getKey(), a -> resp.setCheckNumber(quantityNumberMap.get(entry.getKey())));
                double hours =  (double) workerHoursMap.get(entry.getKey()) / 3600000;
                BigDecimal workhours = new BigDecimal(Double.toString(hours)).setScale(2, RoundingMode.HALF_UP); // 使用默认的四舍五入策略
                MapUtils.findAndThen(workerHoursMap, entry.getKey(), a -> resp.setWorkerHoursNumber(workhours));
                MapUtils.findAndThen(userMap, Long.valueOf(entry.getKey()), a -> resp.setName(a.getNickname()));
                // 数量/工时
                Integer quantityNumber = quantityNumberMap.get(entry.getKey()) == null ? 0 : quantityNumberMap.get(entry.getKey());
                resp.setRate(new BigDecimal(quantityNumber).divide(workhours, 8, RoundingMode.HALF_DOWN).multiply(new BigDecimal(100.00)));
                resps.add(resp);
            }

            Collections.sort(resps, new Comparator<WorkerHoursAnalysisResp>() {
                @Override
                public int compare(WorkerHoursAnalysisResp o1, WorkerHoursAnalysisResp o2) {
                    return o1.getRate().compareTo(o2.getRate());
                }
            });
        }

        return resps;
    }

    @Override
    public ScrapAndRepairResp getScrapAndRepair(AnalysisReqVO vo) {

        List<InspectionSheetSchemeMaterialDO> list = inspectionSheetSchemeMaterialService.getMaterialsByAnalysis(vo);

        ScrapAndRepairResp resp = new ScrapAndRepairResp();

        Integer scrapNumber = 0;

        Integer repairNumber = 0;

        if (!CollectionUtils.isEmpty(list)) {

            for (InspectionSheetSchemeMaterialDO schemeMaterialDO : list) {

                if (schemeMaterialDO.getHandleMethod().intValue()==1){
                    repairNumber++;
                }else if (schemeMaterialDO.getHandleMethod().intValue()==2){
                    scrapNumber++;
                }

            }
        }
        resp.setRepairNumber(repairNumber);
        resp.setScrapNumber(scrapNumber);
        return resp;
    }


}
