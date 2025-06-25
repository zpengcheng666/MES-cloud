package com.miyu.module.qms.service.inspectionsheet;

import cn.hutool.core.util.ObjUtil;
import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.util.collection.CollectionUtils;
import cn.iocoder.yudao.framework.common.util.object.ObjectUtils;
import cn.iocoder.yudao.framework.mybatis.core.util.MyBatisUtils;
import cn.iocoder.yudao.module.system.api.dept.DeptApi;
import cn.iocoder.yudao.module.system.api.dept.dto.DeptRespDTO;
import cn.iocoder.yudao.module.system.api.user.AdminUserApi;
import cn.iocoder.yudao.module.system.api.user.dto.AdminUserRespDTO;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.miyu.module.qms.controller.admin.inspectionsheetscheme.vo.InspectionSchemeReqVO;
import com.miyu.module.qms.controller.admin.inspectionsheetscheme.vo.InspectionSheetSchemePageReqVO;
import com.miyu.module.qms.controller.admin.inspectionsheetschemematerial.vo.InspectionMaterialOutBoundReqVO;
import com.miyu.module.qms.controller.admin.terminal.EncodingService;
import com.miyu.module.qms.dal.dataobject.inspectionscheme.InspectionSchemeDO;
import com.miyu.module.qms.dal.dataobject.inspectionschemeitem.InspectionSchemeItemDO;
import com.miyu.module.qms.dal.dataobject.inspectionsheetgeneratetask.InspectionSheetGenerateTaskDO;
import com.miyu.module.qms.dal.dataobject.inspectionsheetgeneratetaskdetail.InspectionSheetGenerateTaskDetailDO;
import com.miyu.module.qms.dal.dataobject.inspectionsheetsamplingrule.InspectionSheetSamplingRuleDO;
import com.miyu.module.qms.dal.dataobject.inspectionsheetscheme.InspectionSheetSchemeDO;
import com.miyu.module.qms.dal.dataobject.inspectionsheetrecord.InspectionSheetRecordDO;
import com.miyu.module.qms.dal.dataobject.inspectionsheetschemematerial.InspectionSheetSchemeMaterialDO;
import com.miyu.module.qms.dal.dataobject.samplingrule.SamplingRuleDO;
import com.miyu.module.qms.dal.dataobject.samplingruleconfig.SamplingRuleConfigDO;
import com.miyu.module.qms.dal.mysql.inspectionscheme.InspectionSchemeMapper;
import com.miyu.module.qms.dal.mysql.inspectionschemeitem.InspectionSchemeItemMapper;
import com.miyu.module.qms.dal.mysql.inspectionsheetgeneratetask.InspectionSheetGenerateTaskMapper;
import com.miyu.module.qms.dal.mysql.inspectionsheetgeneratetaskdetail.InspectionSheetGenerateTaskDetailMapper;
import com.miyu.module.qms.dal.mysql.inspectionsheetsamplingrule.InspectionSheetSamplingRuleMapper;
import com.miyu.module.qms.dal.mysql.inspectionsheetscheme.InspectionSheetSchemeMapper;
import com.miyu.module.qms.dal.mysql.inspectionsheetrecord.InspectionSheetRecordMapper;
import com.miyu.module.qms.dal.mysql.inspectionsheetschemematerial.InspectionSheetSchemeMaterialMapper;
import com.miyu.module.qms.dal.mysql.samplingrule.SamplingRuleMapper;
import com.miyu.module.qms.dal.mysql.samplingruleconfig.SamplingRuleConfigMapper;
import com.miyu.module.qms.enums.InspectionSelfAssignmentStatusEnum;
import com.miyu.module.qms.enums.InspectionSheetStatusEnum;
import com.miyu.module.qms.enums.ManagementSystemStatusEnum;
import com.miyu.module.wms.api.mateiral.WarehouseApi;
import com.miyu.module.wms.api.mateiral.dto.WarehouseRespDTO;
import com.miyu.module.wms.api.order.OrderApi;
import com.miyu.module.wms.api.order.dto.OrderReqDTO;
import com.miyu.module.wms.enums.DictConstants;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import com.miyu.module.qms.controller.admin.inspectionsheet.vo.*;
import com.miyu.module.qms.dal.dataobject.inspectionsheet.InspectionSheetDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;

import com.miyu.module.qms.dal.mysql.inspectionsheet.InspectionSheetMapper;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.framework.common.util.object.ObjectUtils.cloneIgnoreId;
import static cn.iocoder.yudao.framework.web.core.util.WebFrameworkUtils.getLoginUserId;
import static com.miyu.module.qms.enums.ErrorCodeConstants.*;

/**
 * 检验单 Service 实现类
 *
 * @author Zhangyunfei
 */
@Service
@Validated
public class InspectionSheetServiceImpl implements InspectionSheetService {

    @Resource
    private InspectionSheetMapper inspectionSheetMapper;

    @Resource
    private InspectionSheetSchemeMapper inspectionSheetSchemeMapper;

    @Resource
    private InspectionSchemeMapper inspectionSchemeMapper;

    @Resource
    private InspectionSchemeItemMapper inspectionSchemeItemMapper;

    @Resource
    private InspectionSheetSchemeMaterialMapper inspectionSheetSchemeMaterialMapper;

    @Resource
    private InspectionSheetRecordMapper inspectionSheetRecordMapper;

    @Resource
    private SamplingRuleMapper samplingRuleMapper;

    @Resource
    private SamplingRuleConfigMapper samplingRuleConfigMapper;

    @Resource
    private InspectionSheetSamplingRuleMapper inspectionSheetSamplingRuleMapper;

    @Resource
    private InspectionSheetGenerateTaskMapper inspectionSheetGenerateTaskMapper;

    @Resource
    private InspectionSheetGenerateTaskDetailMapper inspectionSheetGenerateTaskDetailMapper;

    @Resource
    private AdminUserApi userApi;

    @Resource
    private DeptApi deptApi;

    @Resource
    private WarehouseApi warehouseApi;

    @Resource
    private OrderApi orderApi;

    @Resource
    private EncodingService encodingService;

    @Override
    @Transactional
    public String createInspectionSheet(InspectionSheetSaveReqVO createReqVO) {
        // 检验方案类型为生产
        // 验证工序必填
        validInspectionSheetProcessId(createReqVO);
        // 验证检验单编号 （检验单名称和编号从检验方案中获取拼接时间戳）不验证重复
        // validateInspectionSheetNumber(createReqVO);
        // 获取检验方案
        InspectionSchemeDO scheme = validInspectionSheetScheme(createReqVO);
        // 1 插入检验单
        InspectionSheetDO inspectionSheet = BeanUtils.toBean(createReqVO, InspectionSheetDO.class).setStatus(InspectionSheetStatusEnum.TOASSIGN.getStatus()).setSheetName(scheme.getSchemeName()).setSheetNo(encodingService.getDistributionCode());
        inspectionSheetMapper.insert(inspectionSheet);
        // 待检验数量
        Integer inspectionMaterialNum = createReqVO.getQuantity();
        // 找到符合当前方案aql的抽样方案
        List<SamplingRuleConfigDO> ruleConfigDOS = new ArrayList<>();
        // 检测项检测规则对应关系集合
        List<InspectionSheetSamplingRuleDO> sheetSamplingRuleDOList = new ArrayList<>();
        // 检验类型为抽检
        // 抽样规则不为空
        // 检验数量大于1
        if(scheme.getInspectionSheetType() == 1 && StringUtils.isNotBlank(scheme.getSamplingStandardId()) && inspectionMaterialNum > 1) {
            // 获取检验方案规则
            ruleConfigDOS = getSamplingRuleConfig(scheme, createReqVO);
            // 找到符合当前方案aql的抽样方案
            SamplingRuleConfigDO ruleConfigDO = ruleConfigDOS.stream().filter(a -> a.getAcceptanceQualityLimit().compareTo(scheme.getAcceptanceQualityLimit()) == 0).findFirst().orElseThrow(() -> exception(SAMPLING_RULE_CONFIG_NOT_EXISTS));
            // 抽样数量
            inspectionMaterialNum = ruleConfigDO.getSampleSize();
        }

        // 2 插入检验方案
        InspectionSheetSchemeDO inspectionSheetScheme = getInspectionSheetScheme(inspectionSheet, scheme, createReqVO, inspectionMaterialNum);
        // 首检需要生成两个检测任务
        List<InspectionSheetSchemeDO> inspectionSheetSchemeList = new ArrayList<>();
        // 产品需要首检
        // 多生成一个首检任务
        // 20241112 首检不多生成一个检验任务， 生成的检验任务为专检任务
//        if(createReqVO.getNeedFirstInspection() !=  null && createReqVO.getNeedFirstInspection() == 1){
//            inspectionSheetSchemeList.add(cloneIgnoreId(inspectionSheetScheme, o -> {o.setQuantity(1); o.setInspectionQuantity(1); o.setInspectionSheetType(2); o.setNeedFirstInspection(createReqVO.getNeedFirstInspection()); o.setInspectionLevelType(null); o.setSamplingRuleType(null);}));
//        }
        inspectionSheetSchemeList.add(inspectionSheetScheme);
        inspectionSheetSchemeMapper.insertBatch(inspectionSheetSchemeList);

        // 获取检验项目对应的检测规则
        if(ruleConfigDOS.size() > 0) {
            sheetSamplingRuleDOList.addAll(getSchemeSampingRule(ruleConfigDOS, inspectionSheetSchemeList, scheme));
        }

        // 遍历检验方案集合生成对应的检验单
        for(InspectionSheetSchemeDO sheetScheme : inspectionSheetSchemeList) {
            // 3 插入检验单产品
            List<InspectionSheetSchemeMaterialDO> materialList = getInspectionMaterialList(sheetScheme, createReqVO, inspectionMaterialNum);
            inspectionSheetSchemeMaterialMapper.insertBatch(materialList);
            // 4 保存检验记录
            List<InspectionSchemeItemDO> schemeItemDetailList = inspectionSchemeItemMapper.selectListByInspectionSchemeId(scheme.getId());
            List<InspectionSheetRecordDO> recordList = getInspectionSheetRecordList(scheme, sheetScheme, materialList, schemeItemDetailList);
            inspectionSheetRecordMapper.insertBatch(recordList);
            // 获取检验项目
            // 获取检验项目对应的检测规则
            if(ruleConfigDOS.size() > 0){
                sheetSamplingRuleDOList.addAll(getSchemeItemSampingRule(ruleConfigDOS, schemeItemDetailList, sheetScheme));
            }
        }
        // 批量保存检验单抽样规则对照关系
        if(sheetSamplingRuleDOList.size() > 0){
            inspectionSheetSamplingRuleMapper.insertBatch(sheetSamplingRuleDOList);
        }
        // 返回
        return inspectionSheet.getId();
    }

    /**
     * 创建检验单(自检)
     * @param createReqVO
     * @return
     */
    @Override
    public String createInspectionSheetSelfInspection(InspectionSheetSelfCheckSaveReqVO createReqVO) {

        InspectionSheetSaveReqVO reqVO = BeanUtils.toBean(createReqVO, InspectionSheetSaveReqVO.class);
        // 设置方案类型为生产
        reqVO.setSchemeType(2);
        // 设置自检
        reqVO.setIsSelfInspection(1);
        // 检验方案类型为生产
        // 验证工序必填
        validInspectionSheetProcessId(reqVO);
        // 验证检验单编号 （检验单名称和编号从检验方案中获取拼接时间戳）不验证重复
        // validateInspectionSheetNumber(reqVO);
        // 自检传barCode和locationId
        // TODO
        // 需要获取materialConfigId
//        String materialId = "1810231057551753217";
//        String materialConfigId = "1796371639521251329";
//        String batchNumber = "PC001";
//        reqVO.setMaterialId(materialId);
//        reqVO.setMaterialConfigId(materialConfigId);
//        reqVO.setBatchNumber(batchNumber);

        // 验证检验单是否已生成 生产单号 + barCode 确定唯一检验单
        validInspectionSheetExists(reqVO);

        // 获取检验方案
        InspectionSchemeDO scheme = validInspectionSheetScheme(reqVO);

        // 1 插入检验单
        InspectionSheetDO inspectionSheet = BeanUtils.toBean(createReqVO, InspectionSheetDO.class).setStatus(InspectionSheetStatusEnum.TOINSPECT.getStatus()).setSheetName(scheme.getSchemeName()).setSheetNo(encodingService.getDistributionCode());
        // 自检负责人为自己
        inspectionSheet.setHeader(reqVO.getAssignmentId());
        inspectionSheetMapper.insert(inspectionSheet);
        // 2 插入检验方案
        InspectionSheetSchemeDO inspectionSheetScheme = getInspectionSheetScheme(inspectionSheet, scheme, reqVO, 1);
        inspectionSheetSchemeMapper.insert(inspectionSheetScheme);
        // 3 插入检验单产品
        List<InspectionSheetSchemeMaterialDO> materialList = getInspectionMaterialList(inspectionSheetScheme, reqVO, 1);
        inspectionSheetSchemeMaterialMapper.insertBatch(materialList);
        // 4 保存检验记录
        List<InspectionSchemeItemDO> schemeItemDetailList = inspectionSchemeItemMapper.selectListByInspectionSchemeId(scheme.getId());
        List<InspectionSheetRecordDO> recordList = getInspectionSheetRecordList(scheme, inspectionSheetScheme, materialList, schemeItemDetailList);
        inspectionSheetRecordMapper.insertBatch(recordList);
        // 返回
        return inspectionSheet.getId();
    }

    /**
     * 单号查询检验单集合
     * @param numbers
     * @return
     */
    @Override
    public List<InspectionSheetDO> getInspectionSheetListByRecordNumber(Collection<String> numbers) {
        List<InspectionSheetDO> sheetList = inspectionSheetMapper.selectList(InspectionSheetDO::getRecordNumber, numbers);
        return sheetList;
    }

    /**
     * 单号查询检验单产品集合
     * @param number
     * @return
     */
    @Override
    public List<InspectionSheetSchemeMaterialDO> getInspectionSheetMaterialListByRecordNumber(String number) {
        // 单号查询检验单
        // 检验单状态是已完成
        List<InspectionSheetDO> sheetList = inspectionSheetMapper.selectList(InspectionSheetDO::getRecordNumber, number, InspectionSheetDO::getStatus, InspectionSheetStatusEnum.INSPECTED.getStatus());
        if(sheetList.size() > 0){
            List<InspectionSheetSchemeDO> schemeList = inspectionSheetSchemeMapper.selectList(InspectionSheetSchemeDO::getInspectionSheetId, sheetList.get(0).getId());
            // 检验任务id集合 生产只能有一个，采购销售可能是多个
            List<String> ids = schemeList.stream().map(InspectionSheetSchemeDO::getId).collect(Collectors.toList());
            List<InspectionSheetSchemeMaterialDO> materialList = inspectionSheetSchemeMaterialMapper.selectList(InspectionSheetSchemeMaterialDO::getInspectionSheetSchemeId, ids);
            return materialList;
        }
        return new ArrayList<>();
    }

    /**
     * 检测任务id获取检验单
     * @param schemeId
     * @return
     */
    @Override
    public InspectionSheetDO getInspectionSheetBySchemeId(String schemeId) {
        InspectionSheetSchemeDO schemeDO = inspectionSheetSchemeMapper.selectById(schemeId);
        return inspectionSheetMapper.selectById(schemeDO.getInspectionSheetId());
    }

    /**
     * 单号查询检验单产品集合 (批量)
     * @param numbers
     * @return
     */
    @Override
    public List<InspectionSheetSchemeMaterialDO> getInspectionSheetMaterialListByRecordNumberBatch(Collection<String> numbers) {
        // 单号查询检验单
        // 检验单状态是已完成
        List<InspectionSheetDO> sheetList = inspectionSheetMapper.selectList(InspectionSheetDO::getRecordNumber, numbers);
        sheetList = sheetList.stream().filter(o -> InspectionSheetStatusEnum.INSPECTED.getStatus() == o.getStatus()).collect(Collectors.toList());
        if(sheetList.size() > 0){
            List<String> sheetIds = sheetList.stream().map(InspectionSheetDO::getId).collect(Collectors.toList());
            List<InspectionSheetSchemeDO> schemeList = inspectionSheetSchemeMapper.selectList(InspectionSheetSchemeDO::getInspectionSheetId, sheetIds);
            List<String> ids = schemeList.stream().map(InspectionSheetSchemeDO::getId).collect(Collectors.toList());
            List<InspectionSheetSchemeMaterialDO> materialList = inspectionSheetSchemeMaterialMapper.getInspectionMaterialListBySchemeIds(ids);
            return materialList;
        }
        return new ArrayList<>();
    }

    /**
     * 检验单主键获取检验任务集合
     * @param sheetId
     * @return
     */
    @Override
    public List<InspectionSheetSchemeDO> getInspectionSheetSchemeListBySheetId(String sheetId) {
        return inspectionSheetSchemeMapper.selectList(InspectionSheetSchemeDO::getInspectionSheetId, sheetId);
    }

    /**
     * 验证检验计划是否存在
     * @param createReqVO
     */
    private InspectionSchemeDO validInspectionSheetScheme(InspectionSheetSaveReqVO createReqVO) {
        // 获取检验方案
        // InspectionSchemeDO scheme = inspectionSchemeMapper.selectScheme4InspectionSheet(createReqVO);
        InspectionSchemeDO scheme = inspectionSchemeMapper.selectById(createReqVO.getSchemeId());
        // 检验计划是否存在
        if (scheme == null){
            throw exception(INSPECTION_SCHEME_NOT_EXISTS);
        }
        return scheme;
    }

    /**
     * 验证工序ID是否存在
     * @param createReqVO
     */
    private void validInspectionSheetProcessId(InspectionSheetSaveReqVO createReqVO){
        if(createReqVO.getSchemeType() == 2){
            // 工序为空
            if(StringUtils.isBlank(createReqVO.getProcessId())){
                throw exception(INSPECTION_SHEET_PROCESSID_NOT_EXISTS);
            }
        }
    }

    /**
     * 生产单号 + barCode 验证检验单是否存在
     * @param reqVO
     */
    private void validInspectionSheetExists(InspectionSheetSaveReqVO reqVO){
        List<InspectionSheetDO> sheetList = inspectionSheetMapper.selectList(InspectionSheetDO::getRecordNumber, reqVO.getRecordNumber());
        for(InspectionSheetDO sheet : sheetList){
            // 已完成不验证
            if(sheet.getStatus() == InspectionSheetStatusEnum.INSPECTED.getStatus()){
                continue;
            }
            List<InspectionSheetSchemeDO> schemeList = inspectionSheetSchemeMapper.selectList(InspectionSheetSchemeDO::getInspectionSheetId, sheet.getId());
            List<InspectionSheetSchemeMaterialDO> materialList = inspectionSheetSchemeMaterialMapper.selectList(InspectionSheetSchemeMaterialDO::getInspectionSheetSchemeId, schemeList.get(0).getId());
            // 已存在检验单
            if(reqVO.getBarCode().equals(materialList.get(0).getBarCode())) {
                throw exception(INSPECTION_SHEET_EXISTS);
            }
        }
    }

    /**
     * 封装检验单产品集合
     * @param sheetScheme
     * @param createReqVO
     * @param inspectionMaterialNum
     * @return
     */
    private List<InspectionSheetSchemeMaterialDO> getInspectionMaterialList(InspectionSheetSchemeDO sheetScheme, InspectionSheetSaveReqVO createReqVO, Integer inspectionMaterialNum)
    {
        // 检验任务生成的检验单
        if(StringUtils.isNotBlank(createReqVO.getTaskId())){
            List<InspectionSheetGenerateTaskDetailDO> list = inspectionSheetGenerateTaskDetailMapper.selectList(InspectionSheetGenerateTaskDetailDO::getTaskId, createReqVO.getTaskId());

            List<InspectionSheetSchemeMaterialDO> materialList = new ArrayList<>();
            list.stream().forEach(o -> {
                InspectionSheetSchemeMaterialDO material = new InspectionSheetSchemeMaterialDO();
                material.setInspectionSheetSchemeId(sheetScheme.getId());
                material.setMaterialId(o.getMaterialId());
                material.setMaterialConfigId(createReqVO.getMaterialConfigId());
                material.setBarCode(o.getBarCode());
                // material.setBarCodeCheck(o.getBarCode() + "_" + "1");
                material.setBatchNumber(o.getBatchNumber());
                materialList.add(material);
            });
            return materialList;
        }

        InspectionSheetSchemeMaterialDO material = new InspectionSheetSchemeMaterialDO();
        material.setInspectionSheetSchemeId(sheetScheme.getId());
        material.setMaterialConfigId(createReqVO.getMaterialConfigId());
        material.setBatchNumber(createReqVO.getBatchNumber());
        List<InspectionSheetSchemeMaterialDO> materialList = new ArrayList<>();

        // 自检
        if(createReqVO.getIsSelfInspection() != null && createReqVO.getIsSelfInspection() == 1){
            materialList.add(cloneIgnoreId(material, o -> {
                o.setMaterialId(createReqVO.getMaterialId());
                o.setBarCode(createReqVO.getBarCode());
                // o.setBarCodeCheck(createReqVO.getBarCode() + "_" + "1");
            }));
            return materialList;
        }

        /**
         * 20241113 首检产品不多生成一个检验任务，原任务标记为专检（目前只有自检任务）
         */
        // 首检单 生成一个检验产品
//        if(sheetScheme.getNeedFirstInspection() != null && sheetScheme.getNeedFirstInspection() == 1) {
//            materialList.add(cloneIgnoreId(material, o -> {}));
//        }
        // 非首检生成多个检验产品
//        else {
            // 根据检验产品数量生成多个检验单产品
//            for (int i = 0; i < inspectionMaterialNum; i++) {
//                materialList.add(cloneIgnoreId(material, o -> {}));
//            }
//        }

        // 非自检
        for (int i = 0; i < inspectionMaterialNum; i++) {
            materialList.add(cloneIgnoreId(material, o -> {}));
        }
        return materialList;
    }


    /**
     * 封装检验单记录集合
     * @param scheme
     * @param sheetScheme
     * @param materialList
     * @return
     */
    private List<InspectionSheetRecordDO> getInspectionSheetRecordList(InspectionSchemeDO scheme, InspectionSheetSchemeDO sheetScheme, List<InspectionSheetSchemeMaterialDO> materialList, List<InspectionSchemeItemDO> schemeItemDetailList) {
        // 4 插入检验记录
        List<InspectionSheetRecordDO> recordList = new ArrayList<>();
        // 给每个检验产品生成对应的检验记录
        for(InspectionSheetSchemeMaterialDO schemeMaterial :materialList){
            recordList.addAll(BeanUtils.toBean(schemeItemDetailList, InspectionSheetRecordDO.class, vo -> {
                vo.setInspectionSheetSchemeId(sheetScheme.getId());
                vo.setInspectionSchemeItemId(vo.getId());
                vo.setSchemeMaterialId(schemeMaterial.getId());
                vo.setContent(null);
                vo.setId(null);
            }));
        }
        return recordList;
    }

    /**
     * 封装检验单任务
     * @param inspectionSheet
     * @param scheme
     * @param createReqVO
     * @param quantity
     */
    private InspectionSheetSchemeDO getInspectionSheetScheme(InspectionSheetDO inspectionSheet, InspectionSchemeDO scheme, InspectionSheetSaveReqVO createReqVO, Integer quantity) {
        // 2 插入检验方案
        InspectionSheetSchemeDO inspectionSheetScheme = BeanUtils.toBean(scheme, InspectionSheetSchemeDO.class);
        inspectionSheetScheme.setId(null);
        inspectionSheetScheme.setInspectionSheetId(inspectionSheet.getId());
        inspectionSheetScheme.setInspectionSchemeId(scheme.getId());
        // inspectionSheetScheme.setNeedFirstInspection(createReqVO.getNeedFirstInspection());
        inspectionSheetScheme.setPlanTime(LocalDateTime.now());
        inspectionSheetScheme.setBatchNumber(createReqVO.getBatchNumber());
        // 自检
        if(createReqVO.getIsSelfInspection() != null && createReqVO.getIsSelfInspection() == 1) {
            inspectionSheetScheme.setInspectionSheetType(2);
            inspectionSheetScheme.setInspectionQuantity(1);
            inspectionSheetScheme.setQuantity(1);
            inspectionSheetScheme.setStatus(InspectionSheetStatusEnum.TOINSPECT.getStatus());
            inspectionSheetScheme.setSchemeType(2);
            // 分配类型 人员
            inspectionSheetScheme.setAssignmentType(1);
            inspectionSheetScheme.setAssignmentId(createReqVO.getAssignmentId() == null ? getLoginUserId().toString() : createReqVO.getAssignmentId());
            inspectionSheetScheme.setAssignmentDate(LocalDateTime.now());
            inspectionSheetScheme.setSelfInspection(1);
            // 分配互检人员
            // 查询部门领导
            AdminUserRespDTO user = userApi.getUser(getLoginUserId()).getCheckedData();
            DeptRespDTO dept = deptApi.getDept(user.getDeptId()).getCheckedData();
            inspectionSheetScheme.setMutualAssignmentId(dept.getLeaderUserId() == null ? null : dept.getLeaderUserId().toString());
            inspectionSheetScheme.setSelfAssignmentStatus(InspectionSelfAssignmentStatusEnum.TOINSPECT_SELF.getStatus());
            // 是否专检
            inspectionSheetScheme.setIsSpecInspect(scheme.getIsInspect());
            // 是否首检
            inspectionSheetScheme.setNeedFirstInspection(createReqVO.getNeedFirstInspection() == null ? 2 : createReqVO.getNeedFirstInspection());
            // 需要首检的任务需要检验员参与 设置成需要专检
            if(createReqVO.getNeedFirstInspection() != null && createReqVO.getNeedFirstInspection() == 1){
                inspectionSheetScheme.setIsSpecInspect("1");
            }
        }
        else {
            inspectionSheetScheme.setInspectionSheetType(scheme.getInspectionSheetType());
            inspectionSheetScheme.setInspectionQuantity(createReqVO.getQuantity());
            inspectionSheetScheme.setQuantity(quantity);
            inspectionSheetScheme.setStatus(InspectionSheetStatusEnum.TOASSIGN.getStatus());
            // 抽检
            if(scheme.getInspectionSheetType() == 1){
                inspectionSheetScheme.setInspectionLevelType(scheme.getInspectionLevelType());
                inspectionSheetScheme.setSamplingRuleType(scheme.getSamplingRuleType());
            }
        }

        return inspectionSheetScheme;
    }

    /**
     * 获取检验方案规则
     * @param scheme
     * @param createReqVO
     * @return
     */
    private List<SamplingRuleConfigDO> getSamplingRuleConfig(InspectionSchemeDO scheme, InspectionSheetSaveReqVO createReqVO) {
        // 通过检验水平类型获取抽样规则
        List<SamplingRuleDO> ruleDOList = samplingRuleMapper.getSamplingRuleByLevelType(scheme.getSamplingStandardId(), scheme.getInspectionLevelType());
        // 遍历抽样规则集合，找出当前检测数量所对应的抽样规则
        for (SamplingRuleDO ruleDO : ruleDOList) {
            // 检测数量符合的抽样规则
            if(createReqVO.getQuantity().compareTo(ruleDO.getMinValue()) >= 0 && (ruleDO.getMaxValue() == null || createReqVO.getQuantity().compareTo(ruleDO.getMaxValue()) <= 0)){
                // 样本量字码 抽样标准ID 检查类型 获取抽样方案
                return samplingRuleConfigMapper.getSamplingRuleConfigList(ruleDO.getSampleSizeCode(), scheme.getSamplingStandardId(), scheme.getSamplingRuleType());
            }
        }
        return new ArrayList<>();
    }

    /**
     * 获取检验任务对应检测规则关系
     * @param ruleConfigDOS
     * @param inspectionSheetSchemeList
     * @param scheme
     * @return
     */
    private List<InspectionSheetSamplingRuleDO> getSchemeSampingRule(List<SamplingRuleConfigDO> ruleConfigDOS, List<InspectionSheetSchemeDO> inspectionSheetSchemeList, InspectionSchemeDO scheme){

        List<InspectionSheetSamplingRuleDO> sheetSamplingRuleDOList = new ArrayList<>();
        for (InspectionSheetSchemeDO sheetScheme : inspectionSheetSchemeList) {
            // 全检
            if(sheetScheme.getInspectionSheetType() == 2){
                continue;
            }
            // 找到符合当前方案aql的抽样方案
            SamplingRuleConfigDO ruleConfigDO = ruleConfigDOS.stream().filter(a -> a.getAcceptanceQualityLimit().compareTo(scheme.getAcceptanceQualityLimit()) == 0).findFirst().orElseThrow(() -> exception(SAMPLING_RULE_CONFIG_NOT_EXISTS));
            InspectionSheetSamplingRuleDO samplingRuleDO = new InspectionSheetSamplingRuleDO();
            samplingRuleDO.setSamplingRuleConfigId(ruleConfigDO.getId());
            samplingRuleDO.setInspectionSheetSchemeId(sheetScheme.getId());
            samplingRuleDO.setSamplingStandardId(ruleConfigDO.getSamplingStandardId());
            sheetSamplingRuleDOList.add(samplingRuleDO);
        }
        return sheetSamplingRuleDOList;
    }

    /**
     * 获取检测项目对应检验规则关系
     * @param ruleConfigDOS
     * @param schemeItemDetailList
     * @param sheetScheme
     * @return
     */
    private List<InspectionSheetSamplingRuleDO> getSchemeItemSampingRule(List<SamplingRuleConfigDO> ruleConfigDOS, List<InspectionSchemeItemDO> schemeItemDetailList, InspectionSheetSchemeDO sheetScheme){

        List<InspectionSheetSamplingRuleDO> sheetSamplingRuleDOList = new ArrayList<>();
        for (InspectionSchemeItemDO schemeItem : schemeItemDetailList) {
            // 检验项aql为null
            if(schemeItem.getAcceptanceQualityLimit() == null){
                continue;
            }
            // 找到符合当前方案aql的抽样方案
            SamplingRuleConfigDO ruleConfigDO = ruleConfigDOS.stream().filter(a -> a.getAcceptanceQualityLimit().compareTo(schemeItem.getAcceptanceQualityLimit()) == 0).findFirst().orElseThrow(() -> exception(SAMPLING_RULE_CONFIG_NOT_EXISTS));
            InspectionSheetSamplingRuleDO samplingRuleDO = new InspectionSheetSamplingRuleDO();
            samplingRuleDO.setSamplingRuleConfigId(ruleConfigDO.getId());
            samplingRuleDO.setInspectionSchemeItemId(schemeItem.getInspectionItemId());
            samplingRuleDO.setInspectionSheetSchemeId(sheetScheme.getInspectionSchemeId());
            samplingRuleDO.setSamplingStandardId(ruleConfigDO.getSamplingStandardId());
            sheetSamplingRuleDOList.add(samplingRuleDO);
        }
         return sheetSamplingRuleDOList;
    }

    @Override
    public void updateInspectionSheet(InspectionSheetSaveReqVO updateReqVO) {
        // 校验存在
        validateInspectionSheetExists(updateReqVO.getId());
        // 更新
        InspectionSheetDO updateObj = BeanUtils.toBean(updateReqVO, InspectionSheetDO.class);
        inspectionSheetMapper.updateById(updateObj);
    }

    /**
     * 删除检验单并且删除其下面所有的任务
     * @param id 编号
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteInspectionSheet(String id) {
        // 校验存在
        validateInspectionSheetExists(id);
        InspectionSheetDO sheet = inspectionSheetMapper.selectById(id);
        // 检验单状态是检验中不可以删除
        if(ObjectUtils.equalsAny(sheet.getStatus(), InspectionSheetStatusEnum.INSPECTING.getStatus(), InspectionSheetStatusEnum.INSPECTED.getStatus())){
            throw exception(INSPECTION_SHEET_UPDATE_FAIL);
        }
        // 删除检验单
        inspectionSheetMapper.deleteById(id);
        // 检验单ID查询检验单任务集合
        List<InspectionSheetSchemeDO> sheetSchemeList = inspectionSheetSchemeMapper.selectList(InspectionSheetSchemeDO::getInspectionSheetId, id);
        // 删除检验单任务
        inspectionSheetSchemeMapper.deleteByInspectionSheetId(id);
        // 遍历检验单任务
        // 删除各自的产品和检验记录
        for(InspectionSheetSchemeDO sheetScheme : sheetSchemeList){
            // 删除检验单产品
            inspectionSheetRecordMapper.deleteByInspectionSheetSchemeId(sheetScheme.getId());
            // 删除检验单记录
            inspectionSheetSchemeMaterialMapper.deleteByInspectionSheetSchemeId(sheetScheme.getId());
        }
    }

    private void validateInspectionSheetExists(String id) {
        if (inspectionSheetMapper.selectById(id) == null) {
            throw exception(INSPECTION_SHEET_NOT_EXISTS);
        }
    }

    /**
     * 验证检验单编号和名称
     * @param reqVO
     */
    private void validateInspectionSheetNumber(InspectionSheetSaveReqVO reqVO) {
        if (inspectionSheetMapper.selectList(InspectionSheetDO::getSheetNo, reqVO.getSheetNo()).size() > 0) {
            throw exception(INSPECTION_SHEET_NO_DUPLICATE);
        }
        // 检验单名称不验证
        if (inspectionSheetMapper.selectList(InspectionSheetDO::getSheetName, reqVO.getSheetName()).size() > 0) {
            throw exception(INSPECTION_SHEET_NAME_DUPLICATE);
        }
    }

    @Override
    public InspectionSheetDO getInspectionSheet(String id) {
        return inspectionSheetMapper.selectById(id);
    }

    @Override
    public PageResult<InspectionSheetDO> getInspectionSheetPage(InspectionSheetPageReqVO pageReqVO) {
        return inspectionSheetMapper.selectPage(pageReqVO);
    }

    /**
     * 专检领取检验任务获得检验单分页
     * @param pageReqVO
     * @return
     */
    @Override
    public PageResult<InspectionSheetSchemeDO> getInspectionSheetTaskPage(InspectionSheetTaskPageReqVO pageReqVO) {
        // 当前登录人
        AdminUserRespDTO user = userApi.getUser(getLoginUserId()).getData();
        pageReqVO.setDeptId(user.getDeptId().toString());
        return inspectionSheetSchemeMapper.selectTaskPage(pageReqVO);
    }

    /**
     * 查询待领取的检验任务
     * @return
     */
    @Override
    public PageResult<InspectionSheetSchemeDO> getInspectionClaimTaskPage(InspectionSheetTaskPageReqVO pageReqVO) {
        // 当前登录人
        AdminUserRespDTO user = userApi.getUser(getLoginUserId()).getData();
        pageReqVO.setDeptId(user.getDeptId().toString());
        return inspectionSheetSchemeMapper.selectClaimTaskList(pageReqVO);
    }

    /**
     * 查询待检验的任务
     * @return
     */
    @Override
    public PageResult<InspectionSheetSchemeDO> getInspectionTaskPage(InspectionSheetTaskPageReqVO pageReqVO) {
        InspectionSchemeReqVO reqVO = new InspectionSchemeReqVO();
        // 当前登录人
        AdminUserRespDTO user = userApi.getUser(getLoginUserId()).getData();
//        reqVO.setDeptId(user.getDeptId().toString());
//        reqVO.setAssignmentId(getLoginUserId().toString());
        IPage<InspectionSheetSchemeDO> pageResult = inspectionSheetSchemeMapper.selectInspectionTaskList(MyBatisUtils.buildPage(pageReqVO), getLoginUserId().toString());
        return new PageResult<>(pageResult.getRecords(), pageResult.getTotal());
    }

    /**
     * 获取不合格品的任务 (审批状态非 审批中、已完成)
     * @return
     */
    @Override
    public PageResult<InspectionSheetSchemeDO> getUnqualifiedTaskPage(InspectionSheetTaskPageReqVO pageReqVO) {
        InspectionSchemeReqVO reqVO = new InspectionSchemeReqVO();
        // 当前登录人
        AdminUserRespDTO user = userApi.getUser(getLoginUserId()).getData();
//        reqVO.setDeptId(user.getDeptId().toString());
//        reqVO.setAssignmentId(getLoginUserId().toString());

        IPage<InspectionSheetSchemeDO> pageResult =inspectionSheetSchemeMapper.selectUnqualifiedTaskList(MyBatisUtils.buildPage(pageReqVO), getLoginUserId().toString());
        return new PageResult<>(pageResult.getRecords(), pageResult.getTotal());
    }

    // ==================== 子表（检验单方案任务计划） ====================
    @Override
    public PageResult<InspectionSheetSchemeDO> getInspectionSheetSchemePage(InspectionSheetSchemePageReqVO pageReqVO) {
        return inspectionSheetSchemeMapper.selectPage(pageReqVO);
    }

    @Override
    public String createInspectionSheetScheme(InspectionSheetSchemeDO inspectionSheetScheme) {
        // 校验是否已经存在
        if (inspectionSheetSchemeMapper.selectByInspectionSheetId(inspectionSheetScheme.getInspectionSheetId()) != null) {
            throw exception(INSPECTION_SHEET_SCHEME_EXISTS);
        }
        // 插入
        inspectionSheetSchemeMapper.insert(inspectionSheetScheme);
        return inspectionSheetScheme.getId();
    }

    @Override
    public void updateInspectionSheetScheme(InspectionSheetSchemeDO inspectionSheetScheme) {
        // 校验存在
        validateInspectionSheetSchemeExists(inspectionSheetScheme.getId());
        // 更新
        inspectionSheetSchemeMapper.updateById(inspectionSheetScheme);
    }

    @Override
    public void deleteInspectionSheetScheme(String id) {
        // 校验存在
        validateInspectionSheetSchemeExists(id);
        // 删除
        inspectionSheetSchemeMapper.deleteById(id);
    }

    @Override
    public InspectionSheetSchemeDO getInspectionSheetScheme(String id) {
        return inspectionSheetSchemeMapper.selectById(id);
    }

    /**
     * 获取检验单信息
     * @param reqVO
     * @return
     */
    @Override
    public InspectionSheetDO getInspectionSheetInfo(InspectionSheetReqVO reqVO) {
        return inspectionSheetMapper.getInspectionSheetInfo(reqVO);
    }

    /**
     * 检验单任务ID获取检验单信息
     * @param schemeId
     * @return
     */
    @Override
    public InspectionSheetSchemeDO getInspectionSheetInfoBySchemeId(String schemeId) {
        return inspectionSheetSchemeMapper.getInspectionSheetInfoBySchemeId(schemeId);
    }

    /**
     * 物料条码和批次号获取检验单信息
     * @param reqVO
     * @return
     */
    @Override
    public List<InspectionSheetSchemeMaterialDO> getInspectionSheetInfoMaterial(InspectionSheetMaterialReqVO reqVO) {
        return inspectionSheetSchemeMaterialMapper.getInspectionSheetInfoMaterial(reqVO);
    }

    private InspectionSheetSchemeDO validateInspectionSheetSchemeExists(String id) {
        InspectionSheetSchemeDO sheetSchemeDO = inspectionSheetSchemeMapper.selectById(id);
        if (sheetSchemeDO == null) {
            throw exception(INSPECTION_SHEET_SCHEME_NOT_EXISTS);
        }
        return sheetSchemeDO;
    }

    /**
     * 检验叫料，通知 wms 出库
     * @param req
     */
    @Override
    public Boolean outBoundInspection(InspectionMaterialOutBoundReqVO req) {
        // 仓库编码获取仓库信息
        List<WarehouseRespDTO> warehouseList = warehouseApi.getWarehouseByCode(req.getWarehouseCode()).getCheckedData();
        // 未查询到仓库
        if(warehouseList.size() == 0){
            throw exception(WAREHOUSE_NOT_EXISTS);
        }
        // 检验单物料id集合查询物料
        List<InspectionSheetSchemeMaterialDO> materialList = inspectionSheetSchemeMaterialMapper.selectList(InspectionSheetSchemeMaterialDO::getId, req.getIds());
        // 检验任务主键查询检验单
        InspectionSheetDO sheetDO = getInspectionSheetBySchemeId(materialList.get(0).getInspectionSheetSchemeId());
        // 封装订单
        List<OrderReqDTO> orderReqDTOList = CollectionUtils.convertList(materialList, detailDO ->
        {
            OrderReqDTO orderRespVO = new OrderReqDTO();
            orderRespVO.setOrderNumber(sheetDO.getSheetNo());
            orderRespVO.setOrderType(DictConstants.WMS_ORDER_TYPE_CHECK_OUT);
            orderRespVO.setChooseStockId(detailDO.getMaterialId());
            // 待出库
            orderRespVO.setOrderStatus(1);
            orderRespVO.setQuantity(1);
            //目标仓库
            orderRespVO.setTargetWarehouseId(warehouseList.get(0).getId());
            return orderRespVO;
        });

        CommonResult<List<String>> result = orderApi.orderDistribute(orderReqDTOList);
        if (result.isSuccess()) {
            return  true;
        } else {
            throw exception(INSPECTION_OUTBOUND_ERROR);
        }
    }


    /**
     * 创建检验单(自检)
     * @param createReqVO
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public String createInspectionSheetTask(InspectionSheetSaveReqVO createReqVO) {
        // 创建检验单
        String id = createInspectionSheet(createReqVO);
        // 更新检验单任务状态为已生成
        InspectionSheetGenerateTaskDO taskDO = new InspectionSheetGenerateTaskDO();
        taskDO.setId(createReqVO.getTaskId());
        taskDO.setStatus(1);
        inspectionSheetGenerateTaskMapper.updateById(taskDO);
        return id;
    }

    /**
     * 更新不合格品审批状态
     * @param businessKey
     * @param status
     */
    @Override
    public void updateUnqualifiedAuditStatus(String businessKey, Integer status) {

        // 1 校验状态是否在审批
        InspectionSheetSchemeDO sheetSchemeDO = validateInspectionSheetSchemeExists(businessKey);
        // 2 只有审批中，可以更新审批结果
        if (ObjUtil.notEqual(sheetSchemeDO.getProcessStatus(), ManagementSystemStatusEnum.PROCESS.getStatus())) {
            throw exception(UNQUALIFIED_MATERIAL_FAIL_NOT_PROCESS);
        }

        InspectionSheetSchemeDO updateObj = new InspectionSheetSchemeDO().setId(businessKey).setProcessStatus(status);
        // 更新付款审批结果
        inspectionSheetSchemeMapper.updateById(updateObj);
    }
}
