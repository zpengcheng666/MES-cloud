package com.miyu.module.qms.service.inspectionsheetrecord;

import cn.hutool.core.util.ObjUtil;
import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.util.object.ObjectUtils;
import com.miyu.cloud.mcs.api.McsManufacturingControlApi;
import com.miyu.cloud.mcs.dto.manufacture.McsBatchRecordDTO;
import com.miyu.cloud.mcs.dto.manufacture.McsBatchRecordEventDTO;
import com.miyu.module.mcc.api.materialconfig.MaterialMCCApi;
import com.miyu.module.qms.controller.admin.analysis.vo.AnalysisReqVO;
import cn.iocoder.yudao.module.bpm.api.task.BpmProcessInstanceApi;
import cn.iocoder.yudao.module.bpm.api.task.dto.BpmProcessInstanceCreateReqDTO;
import com.miyu.module.qms.controller.admin.inspectionsheet.vo.InspectionSheetUpdateAuditReqVO;
import com.miyu.module.qms.controller.admin.inspectionsheet.vo.InspectionSheetUpdateReqVO;
import com.miyu.module.qms.controller.admin.inspectionsheet.vo.InspectionSheetUpdateTerminalReqVO;
import com.miyu.module.qms.controller.admin.unqualifiedregistration.vo.UnqualifiedRegistrationSaveReqVO;
import com.miyu.module.qms.dal.dataobject.inspectionsheet.InspectionSheetDO;
import com.miyu.module.qms.dal.dataobject.inspectionsheetscheme.InspectionSheetSchemeDO;
import com.miyu.module.qms.dal.dataobject.inspectionsheetschemematerial.InspectionSheetSchemeMaterialDO;
import com.miyu.module.qms.dal.mysql.inspectionsheet.InspectionSheetMapper;
import com.miyu.module.qms.dal.mysql.inspectionsheetscheme.InspectionSheetSchemeMapper;
import com.miyu.module.qms.dal.mysql.inspectionsheetschemematerial.InspectionSheetSchemeMaterialMapper;
import com.miyu.module.qms.enums.*;
import com.miyu.module.qms.service.unqualifiedregistration.UnqualifiedRegistrationService;
import com.miyu.module.mcc.api.materialconfig.dto.MaterialConfigRespDTO;
import com.miyu.module.wms.api.mateiral.MaterialStockApi;
import com.miyu.module.wms.api.mateiral.dto.MaterialQualityCheckStatus;
import com.miyu.module.wms.api.mateiral.dto.MaterialStockRespDTO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import com.miyu.module.qms.controller.admin.inspectionsheetrecord.vo.*;
import com.miyu.module.qms.dal.dataobject.inspectionsheetrecord.InspectionSheetRecordDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;

import com.miyu.module.qms.dal.mysql.inspectionsheetrecord.InspectionSheetRecordMapper;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.framework.security.core.util.SecurityFrameworkUtils.getLoginUserId;
import static com.miyu.cloud.mcs.enums.DictConstants.MCS_BATCH_RECORD_STATUS_ONGOING;
import static com.miyu.module.qms.enums.ApiConstants.PROCESS_KEY;
import static com.miyu.module.qms.enums.ErrorCodeConstants.*;
import static com.miyu.module.wms.enums.DictConstants.WMS_MATERIAL_STATUS_QUALIFIED;
import static com.miyu.module.wms.enums.DictConstants.WMS_MATERIAL_STATUS_UNQUALIFIED;

/**
 * 检验记录 Service 实现类
 *
 * @author Zhangyunfei
 */
@Service
@Validated
public class InspectionSheetRecordServiceImpl implements InspectionSheetRecordService {

    @Resource
    private InspectionSheetRecordMapper inspectionSheetRecordMapper;

    @Resource
    private InspectionSheetMapper inspectionSheetMapper;

    @Resource
    private InspectionSheetSchemeMapper inspectionSheetSchemeMapper;

    @Resource
    private InspectionSheetSchemeMaterialMapper inspectionSheetSchemeMaterialMapper;

    @Resource
    private UnqualifiedRegistrationService unqualifiedRegistrationService;

    @Resource
    private MaterialMCCApi materialMCCApi;

    @Resource
    private MaterialStockApi materialStockApi;

    @Resource
    private BpmProcessInstanceApi bpmProcessInstanceApi;

    @Resource
    private McsManufacturingControlApi mcsManufacturingControlApi;


    @Override
    public String createInspectionSheetRecord(InspectionSheetRecordSaveReqVO createReqVO) {
        // 插入
        InspectionSheetRecordDO inspectionSheetRecord = BeanUtils.toBean(createReqVO, InspectionSheetRecordDO.class);
        inspectionSheetRecordMapper.insert(inspectionSheetRecord);
        // 返回
        return inspectionSheetRecord.getId();
    }

    @Override
    public void updateInspectionSheetRecord(InspectionSheetRecordSaveReqVO updateReqVO) {
        // 校验存在
        validateInspectionSheetRecordExists(updateReqVO.getId());
        // 更新
        InspectionSheetRecordDO updateObj = BeanUtils.toBean(updateReqVO, InspectionSheetRecordDO.class);
        inspectionSheetRecordMapper.updateById(updateObj);
    }

    @Override
    public void deleteInspectionSheetRecord(String id) {
        // 校验存在
        validateInspectionSheetRecordExists(id);
        // 删除
        inspectionSheetRecordMapper.deleteById(id);
    }

    private void validateInspectionSheetRecordExists(String id) {
        if (inspectionSheetRecordMapper.selectById(id) == null) {
            throw exception(INSPECTION_SHEET_RECORD_NOT_EXISTS);
        }
    }

    @Override
    public InspectionSheetRecordDO getInspectionSheetRecord(String id) {
        return inspectionSheetRecordMapper.selectById(id);
    }

    @Override
    public PageResult<InspectionSheetRecordDO> getInspectionSheetRecordPage(InspectionSheetRecordPageReqVO pageReqVO) {
        return inspectionSheetRecordMapper.selectPage(pageReqVO);
    }

    /**
     * 检验单产品id获得检验记录集合
     * @param materialId
     * @return
     */
    @Override
    public List<InspectionSheetRecordDO> getInspectionSheetRecordListByMaterialId(String materialId) {
        return inspectionSheetRecordMapper.selectListByMaterialId(materialId);
    }

    /**
     * 产品检验
     * @param updateReqVO
     * @return
     */
    @Override
    @Transactional
    public void updateInspectionRecord(InspectionSheetUpdateReqVO updateReqVO) {
        // 主键获取检验单任务
        InspectionSheetSchemeDO sheetScheme = inspectionSheetSchemeMapper.selectById(updateReqVO.getSheetSchemeId());
        // 检验任务状态不是待检验
        if(!ObjectUtils.equalsAny(sheetScheme.getStatus(), InspectionSheetSchemeStatusEnum.TOINSPECT.getStatus(), InspectionSheetSchemeStatusEnum.INSPECTING.getStatus())){
            throw exception(INSPECTION_SHEET_SCHEME_STATUS_ERROR);
        }

        // 物料条码查询库存
        List<MaterialStockRespDTO> materialStockList = materialStockApi.getMaterialsByBarCode(updateReqVO.getBarCode()).getCheckedData();
        // 物料条码未查询到对应库存
        if(materialStockList.size() == 0){
            throw exception(INSPECTION_SHEET_SCHEME_MATERIAL_STOCK_NOT_EXISTS);
        }
//        else {
//            if(!sheetScheme.getBatchNumber().equals(materialStockList.get(0).getBatchNumber())) {
//                throw exception(INSPECTION_SHEET_SCHEME_MATERIAL_BATCH_ERROR);
//            }
//        }

        // 检验单任务ID查询检验产品集合
        List<InspectionSheetSchemeMaterialDO> materialList = inspectionSheetSchemeMaterialMapper.selectList(InspectionSheetSchemeMaterialDO::getInspectionSheetSchemeId, updateReqVO.getSheetSchemeId());
        // 物料管理模式为单件 验证物料条码不能重复
        Integer materialManage = validMaterialBarCode(sheetScheme, materialList, updateReqVO.getBarCode());

        // 1 更新检验记录
        updateInspectionSheetRecord(updateReqVO, sheetScheme);

        // 2 更新检验产品
        updateInspectionSheetMaterial(updateReqVO, sheetScheme, materialStockList, materialList, materialManage);

        // 3 更新检验任务 开始
        updateInspectionSchemeStart(updateReqVO, sheetScheme);

        // 4 更新检验任务 结束
        updateInspectionSchemeFinish(updateReqVO, sheetScheme);

        // 5 更新检验单状态
        updateInspectionSheet(updateReqVO);

        // 6 物料质检状态更新
        updateMaterialQualityCheckStatus(updateReqVO);

        // 7 工序检开工、完工、暂停
        updateMcsRecordEnd(updateReqVO, sheetScheme);
    }

    /**
     * 工序检完工 暂停
     * @param updateReqVO
     * @param sheetScheme
     */
    private void updateMcsRecordEnd(InspectionSheetUpdateReqVO updateReqVO, InspectionSheetSchemeDO sheetScheme) {

        InspectionSheetDO sheet = inspectionSheetMapper.selectById(sheetScheme.getInspectionSheetId());
        // 检验任务非已完成
        if(sheetScheme.getStatus() != InspectionSheetStatusEnum.INSPECTED.getStatus()){
            return;
        }
        // 单据来源生产
        // 非自检
        if(sheet.getSourceType() == InspectionSheetSourceTypeEnum.PRODUCE.getStatus() && sheetScheme.getSelfInspection()== null){
            McsBatchRecordEventDTO batchRecord = new McsBatchRecordEventDTO();
            batchRecord.setBatchRecordId(sheet.getRecordId());
            batchRecord.setBarCode(updateReqVO.getBarCode());
            batchRecord.setOperatorId(getLoginUserId().toString());
            batchRecord.setDeviceUnitId(updateReqVO.getDeviceId());
            // 非暂存 完工
            if(StringUtils.isBlank(updateReqVO.getTempSave())){
                // 开工
                CommonResult<?> result = mcsManufacturingControlApi.batchRecordEnd(batchRecord);
                if(!result.isSuccess()){
                    throw exception(result.getCode(), result.getMsg());
                }
            }
            // 暂停
//            else {
//
//            }
        }
    }

    /**
     * 更新检验记录
     */
    private void updateInspectionSheetRecord(InspectionSheetUpdateReqVO updateReqVO, InspectionSheetSchemeDO sheetScheme)
    {
        // 自检任务
        if(sheetScheme.getSelfInspection() != null){
            // 待自检
            if (ObjUtil.equal(sheetScheme.getSelfAssignmentStatus(), InspectionSelfAssignmentStatusEnum.TOINSPECT_SELF.getStatus())) {
                inspectionSheetRecordMapper.updateBatch(BeanUtils.toBean(updateReqVO.getRecords(), InspectionSheetRecordDO.class));
            }
            // 待互检
            else if(ObjUtil.equal(sheetScheme.getSelfAssignmentStatus(), InspectionSelfAssignmentStatusEnum.TOINSPECT_MUTUAL.getStatus())){
                inspectionSheetRecordMapper.updateBatch(BeanUtils.toBean(updateReqVO.getRecords(), InspectionSheetRecordDO.class, o -> {
                    o.setMutualInspectionResult(o.getInspectionResult());
                    o.setMutualContent(o.getContent());
                    o.setInspectionResult(null);
                    o.setContent(null);
                }));
            }
            //  专检
            else if (ObjUtil.equal(sheetScheme.getSelfAssignmentStatus(), InspectionSelfAssignmentStatusEnum.TOINSPECT_SPEC.getStatus())) {
                inspectionSheetRecordMapper.updateBatch(BeanUtils.toBean(updateReqVO.getRecords(), InspectionSheetRecordDO.class, o -> {
                    o.setSpecInspectionResult(o.getInspectionResult());
                    o.setSpecContent(o.getContent());
                    o.setInspectionResult(null);
                    o.setContent(null);
                }));
            }
        }
        else {
            // 更新检验记录
            inspectionSheetRecordMapper.updateBatch(BeanUtils.toBean(updateReqVO.getRecords(), InspectionSheetRecordDO.class));
        }
    }


    /**
     * 更新检验产品
     */
    private void updateInspectionSheetMaterial(InspectionSheetUpdateReqVO updateReqVO, InspectionSheetSchemeDO sheetScheme, List<MaterialStockRespDTO> materialStockList, List<InspectionSheetSchemeMaterialDO> materialList, Integer materialManage) {
        // 更新检测产品ID
        InspectionSheetSchemeMaterialDO material = new InspectionSheetSchemeMaterialDO();
        material.setId(updateReqVO.getSheetSchemeMaterialId());
        material.setContent(updateReqVO.getContent());

        // 非自检任务
        // 自检任务且状态为待自检
        if(sheetScheme.getSelfInspection() == null)
        {
            material.setBarCode(updateReqVO.getBarCode());
            material.setMaterialId(materialStockList.get(0).getId());
            material.setInspectionResult(updateReqVO.getInspectionResult());

            // 非暂存 非自检 将产品状态更新成已完成
            if(StringUtils.isBlank(updateReqVO.getTempSave())) {
                material.setStatus(1);
            }

            // 物料管理模式为批量
            // 处理条码
            if(materialManage != null && materialManage == 2) {
                // 过滤出当前检验任务下产品物料类型为当前检验产品的物料类型集合
                List<InspectionSheetSchemeMaterialDO> cMaterialList = materialList.stream().filter(a -> a.getMaterialConfigId().equals(materialStockList.get(0).getMaterialConfigId())).collect(Collectors.toList());
                // 当前检验产品物料类型在当前检验任务产品下只有一个
                // 把当前条码直接赋值
                if(cMaterialList.size() == 1){
                    material.setBarCodeCheck(updateReqVO.getBarCode());
                }
                else {
                    // 过滤出未检验的产品
                    material.setBarCodeCheck(updateReqVO.getBarCode() + "_" + (materialList.stream().filter(a -> StringUtils.isNotBlank(a.getBarCodeCheck()) && a.getMaterialConfigId().equals(materialStockList.get(0).getMaterialConfigId())).collect(Collectors.toList()).size() + 1));
                }
            }
            else {
                material.setBarCodeCheck(updateReqVO.getBarCode());
            }
        }
        // 自检任务 状态为互检或专检
        else {
            // 自检结果
            if(ObjUtil.equal(sheetScheme.getSelfAssignmentStatus(), InspectionSelfAssignmentStatusEnum.TOINSPECT_SELF.getStatus())){
                material.setInspectionResult(updateReqVO.getInspectionResult());
                material.setBarCodeCheck(updateReqVO.getBarCode());
            }
            // 互检结果
            if(ObjUtil.equal(sheetScheme.getSelfAssignmentStatus(), InspectionSelfAssignmentStatusEnum.TOINSPECT_MUTUAL.getStatus())){
                material.setMutualInspectionResult(updateReqVO.getInspectionResult());
            }
            // 专检结果
            if(ObjUtil.equal(sheetScheme.getSelfAssignmentStatus(), InspectionSelfAssignmentStatusEnum.TOINSPECT_SPEC.getStatus())){
                material.setSpecInspectionResult(updateReqVO.getInspectionResult());
            }
            // 非暂存
            if(StringUtils.isBlank(updateReqVO.getTempSave())){
                // 自检完成
                if (ObjUtil.equal(sheetScheme.getSelfAssignmentStatus(), InspectionSelfAssignmentStatusEnum.TOINSPECT_SELF.getStatus())) {
                    material.setSelfStatus(1);
                }
                // 互检完成
                if (ObjUtil.equal(sheetScheme.getSelfAssignmentStatus(), InspectionSelfAssignmentStatusEnum.TOINSPECT_MUTUAL.getStatus())) {
                    material.setMutualStatus(1);
                    // 不需要专检
                    if("0".equals(sheetScheme.getIsSpecInspect()) || StringUtils.isBlank(sheetScheme.getIsSpecInspect())){
                        material.setStatus(1);
                    }
                }
                //  检验任务需要专检 且 专检完成
                //  将产品状态更新成已完成
                if (ObjUtil.equal(sheetScheme.getSelfAssignmentStatus(), InspectionSelfAssignmentStatusEnum.TOINSPECT_SPEC.getStatus())) {
                    material.setSpecStatus(1);
                    material.setStatus(1);
                }
            }
        }

        inspectionSheetSchemeMaterialMapper.updateById(material);
    }

    /**
     * 更新检验任务状态 开始
     */
    private void updateInspectionSchemeStart(InspectionSheetUpdateReqVO updateReqVO, InspectionSheetSchemeDO sheetScheme) {
        // 任务未开始
        if (sheetScheme.getBeginTime() == null) {
            InspectionSheetSchemeDO updSheetScheme = new InspectionSheetSchemeDO();
            updSheetScheme.setId(updateReqVO.getSheetSchemeId());
            // 开始时间
            updSheetScheme.setBeginTime(LocalDateTime.now());
            updSheetScheme.setStatus(InspectionSheetSchemeStatusEnum.INSPECTING.getStatus());
            inspectionSheetSchemeMapper.updateById(updSheetScheme);
        }
    }

    /**
     * 更新检验任务 结束
     */
    private void updateInspectionSchemeFinish(InspectionSheetUpdateReqVO updateReqVO, InspectionSheetSchemeDO sheetScheme)
    {
        // 检验单任务ID查询检验产品集合
        List<InspectionSheetSchemeMaterialDO> materialList = inspectionSheetSchemeMaterialMapper.selectList(InspectionSheetSchemeMaterialDO::getInspectionSheetSchemeId, updateReqVO.getSheetSchemeId());
        // 过滤未检测的记录（不包含当前产品）
        List<InspectionSheetSchemeMaterialDO> toInspectList;
        // 自检任务 查看最终专检结果
        if(sheetScheme.getSelfInspection() != null ){
            toInspectList = materialList.stream().filter(s -> s.getStatus() == null).collect(Collectors.toList());
        }
        else {
            toInspectList = materialList.stream().filter(s -> s.getStatus() == null && !s.getId().equals(updateReqVO.getSheetSchemeMaterialId())).collect(Collectors.toList());
        }

        InspectionSheetSchemeDO updSheetScheme = new InspectionSheetSchemeDO();
        updSheetScheme.setId(updateReqVO.getSheetSchemeId());
        // 非暂存
        // 当前检测任务全部都检测
        // 更新结束时间并完成当前产品检测任务
        if(StringUtils.isBlank(updateReqVO.getTempSave()) && toInspectList.size() == 0){
            // 检验结束时间
            updSheetScheme.setEndTime(LocalDateTime.now());
            // 检验任务状态为已完成
            updSheetScheme.setStatus(InspectionSheetSchemeStatusEnum.INSPECTED.getStatus());
            // 1 自检单
            // 2 检验任务需专检且状态为 专检 或检验任务不需要专检且状态为 互检
            // 3 检验结果为不合格
            // 将自检任务分配状态设置为待分配
//            if(sheetScheme.getSelfInspection() != null && updateReqVO.getInspectionResult() == 2 ){
//                // 检验任务需要专检
//                if(!"0".equals(sheetScheme.getIsSpecInspect()) && ObjUtil.equal(sheetScheme.getSelfAssignmentStatus(), InspectionSelfAssignmentStatusEnum.TOINSPECT_SPEC.getStatus())){
//                    updSheetScheme.setSelfAssignmentStatus(InspectionSelfAssignmentStatusEnum.TOASSIGN.getStatus());
//                }
//                // 互检
//                if("0".equals(sheetScheme.getIsSpecInspect()) && ObjUtil.equal(sheetScheme.getSelfAssignmentStatus(), InspectionSelfAssignmentStatusEnum.TOINSPECT_MUTUAL.getStatus())){
//                    updSheetScheme.setSelfAssignmentStatus(InspectionSelfAssignmentStatusEnum.TOASSIGN.getStatus());
//
//                }
//            }

            // 获取检验合格产品数量
            List<InspectionSheetSchemeMaterialDO> qualifiedlist;
            // 自检任务 合格数量需要统计专检结果合格的
            if(sheetScheme.getSelfInspection() != null ){
                // 专检结果
                if(!"0".equals(sheetScheme.getIsSpecInspect())){
                    qualifiedlist = inspectionSheetSchemeMaterialMapper.selectList(InspectionSheetSchemeMaterialDO::getInspectionSheetSchemeId, updSheetScheme.getId(), InspectionSheetSchemeMaterialDO::getSpecInspectionResult, "1");
                }
                // 互检结果
                else {
                    qualifiedlist = inspectionSheetSchemeMaterialMapper.selectList(InspectionSheetSchemeMaterialDO::getInspectionSheetSchemeId, updSheetScheme.getId(), InspectionSheetSchemeMaterialDO::getMutualInspectionResult, "1");
                }
            }
            else {
                qualifiedlist = inspectionSheetSchemeMaterialMapper.selectList(InspectionSheetSchemeMaterialDO::getInspectionSheetSchemeId, updSheetScheme.getId(), InspectionSheetSchemeMaterialDO::getInspectionResult, "1");
            }

            // 合格数量
            updSheetScheme.setQualifiedQuantity(qualifiedlist.size());
            // 存在不合格产品
            if(sheetScheme.getQuantity() != sheetScheme.getQualifiedQuantity()){
                updSheetScheme.setProcessStatus(UnqualifiedAuditStatusEnum.DRAFT.getStatus());
            }

            inspectionSheetSchemeMapper.updateById(updSheetScheme);
        }

        // 自检任务
        if(sheetScheme.getSelfInspection() != null) {
            // 非暂存
            if(StringUtils.isBlank(updateReqVO.getTempSave())){
                // 自检完成将任务状态更新为 待互检
                if(ObjUtil.equal(sheetScheme.getSelfAssignmentStatus(), InspectionSelfAssignmentStatusEnum.TOINSPECT_SELF.getStatus())){
                    updSheetScheme.setSelfAssignmentStatus(InspectionSelfAssignmentStatusEnum.TOINSPECT_MUTUAL.getStatus());
                }
                // 互检完成且当前任务需要专检
                // 将任务状态更新为 待认领
                else if(ObjUtil.equal(sheetScheme.getSelfAssignmentStatus(), InspectionSelfAssignmentStatusEnum.TOINSPECT_MUTUAL.getStatus())){
                    if(!"0".equals(sheetScheme.getIsSpecInspect())){
                        updSheetScheme.setSelfAssignmentStatus(InspectionSelfAssignmentStatusEnum.TOCLAIM.getStatus());
                    }
                }
                inspectionSheetSchemeMapper.updateById(updSheetScheme);
            }
        }
    }

    /**
     *  更新检验单状态
     */
    private void updateInspectionSheet(InspectionSheetUpdateReqVO updateReqVO)
    {
        // 获取当前检验单
        InspectionSheetDO sheet = inspectionSheetMapper.selectById(updateReqVO.getSheetId());
        // 获取检验单下的所有检验任务
        List<InspectionSheetSchemeDO> schemeList = inspectionSheetSchemeMapper.selectList(InspectionSheetSchemeDO::getInspectionSheetId, updateReqVO.getSheetId());
        // 最小的状态
        Integer minStatus = schemeList.stream().mapToInt(InspectionSheetSchemeDO::getStatus).min().getAsInt();
        // 任务未开始或检验任务已完成
        if(sheet.getBeginTime() == null || sheet.getStatus().compareTo(minStatus) < 0){
            InspectionSheetDO updSheet = new InspectionSheetDO();
            updSheet.setId(updateReqVO.getSheetId());
            // 检验单未开始
            if(sheet.getBeginTime() == null){
                updSheet.setBeginTime(LocalDateTime.now());
            }
            // 当前状态大于检验单状态需要更新检验单状态
            if(sheet.getStatus().compareTo(minStatus) < 0){
                updSheet.setStatus(minStatus);
            }

            if(InspectionSheetSchemeStatusEnum.INSPECTED.getStatus().compareTo(minStatus) == 0){
                updSheet.setStatus(InspectionSheetStatusEnum.INSPECTED.getStatus());
                updSheet.setEndTime(LocalDateTime.now());
            }
            inspectionSheetMapper.updateById(updSheet);
        }
    }


    /**
     * 物料质检状态更新
     * @param updateReqVO
     */
    private void updateMaterialQualityCheckStatus(InspectionSheetUpdateReqVO updateReqVO) {

        // 获取检验单产品
        InspectionSheetSchemeMaterialDO materialDO = inspectionSheetSchemeMaterialMapper.selectById(updateReqVO.getSheetSchemeMaterialId());
        // 检验单产品非已完成
        if(materialDO.getStatus() != null && materialDO.getStatus() != 1){
            return;
        }

        // 检验结果不为空 更新检验结果
        if(updateReqVO.getInspectionResult() != null){
            List<MaterialQualityCheckStatus> materialQualityCheckStatusList = new ArrayList<>();
            MaterialQualityCheckStatus materialQualityCheckStatus = new MaterialQualityCheckStatus();
            materialQualityCheckStatus.setBarCode(updateReqVO.getBarCode());
            materialQualityCheckStatus.setMaterialStatus(updateReqVO.getInspectionResult() == 1 ? WMS_MATERIAL_STATUS_QUALIFIED : WMS_MATERIAL_STATUS_UNQUALIFIED);
            materialQualityCheckStatusList.add(materialQualityCheckStatus);
            CommonResult<Boolean> commonResult =  materialStockApi.updateQualityCheckStatus(materialQualityCheckStatusList);
            // 更新质检状态失败
            if(!commonResult.isSuccess()){
                throw exception(MATERIAL_STOCK_INSPECTION_STATUS_ERROR);
            }
        }
    }

    /**
     * 自检提交审核
     * 自检不能提交审核 已作废
     * @param updateReqVO
     */
    @Override
    @Transactional
    public void updateInspectionRecordAndAudit(InspectionSheetUpdateAuditReqVO updateReqVO) {
        // 主键获取检验单任务
        InspectionSheetSchemeDO sheetScheme = inspectionSheetSchemeMapper.selectById(updateReqVO.getSheetSchemeId());
        // 检验任务状态不是待检验
        if(!ObjectUtils.equalsAny(sheetScheme.getStatus(), InspectionSheetSchemeStatusEnum.TOINSPECT.getStatus(), InspectionSheetSchemeStatusEnum.INSPECTING.getStatus())){
            throw exception(INSPECTION_SHEET_SCHEME_STATUS_ERROR);
        }
        // 更新检验记录
        inspectionSheetRecordMapper.updateBatch(BeanUtils.toBean(updateReqVO.getRecords(), InspectionSheetRecordDO.class));

        // 更新检测产品ID
        InspectionSheetSchemeMaterialDO material = new InspectionSheetSchemeMaterialDO();
        material.setId(updateReqVO.getSheetSchemeMaterialId());
        material.setInspectionResult(updateReqVO.getInspectionResult());
        material.setContent(updateReqVO.getContent());
        material.setStatus(1);
        inspectionSheetSchemeMaterialMapper.updateById(material);

        // 检验单任务状态设置成已完成
        InspectionSheetSchemeDO updSheetScheme = new InspectionSheetSchemeDO();
        updSheetScheme.setId(updateReqVO.getSheetSchemeId());
        updSheetScheme.setBeginTime(LocalDateTime.now());
        updSheetScheme.setEndTime(LocalDateTime.now());
        // 检验结果合格 合格数量1,不合格(数量为0)
        updSheetScheme.setQualifiedQuantity(updateReqVO.getInspectionResult() == 1 ? 1 : 0);
        updSheetScheme.setInspectionResult(updateReqVO.getInspectionResult());
        updSheetScheme.setStatus(InspectionSheetSchemeStatusEnum.INSPECTED.getStatus());
        inspectionSheetSchemeMapper.updateById(updSheetScheme);

        // 检验单状态设置成已完成
        InspectionSheetDO updSheet = new InspectionSheetDO();
        updSheet.setId(updateReqVO.getSheetId());
        updSheet.setBeginTime(LocalDateTime.now());
        updSheet.setEndTime(LocalDateTime.now());
        updSheet.setStatus(InspectionSheetStatusEnum.INSPECTED.getStatus());
        inspectionSheetMapper.updateById(updSheet);

        // 保存不合格品登记
        UnqualifiedRegistrationSaveReqVO unqualifiedRegistration = BeanUtils.toBean(updateReqVO, UnqualifiedRegistrationSaveReqVO.class);
        unqualifiedRegistration.setInspectionSheetSchemeId(updateReqVO.getSheetSchemeId());


        // todo
        // 封装不合格产品
        unqualifiedRegistration.getRegistrations().stream().forEach(
                o -> {
                    o.setDefectiveCode(new ArrayList<>(Arrays.asList(updateReqVO.getSheetSchemeMaterialId())));
                }
        );
        unqualifiedRegistrationService.saveUnqualifiedRegistrationBatch(unqualifiedRegistration);

        // 启动审批流程
        // 2. 创建合同审批流程实例
        Map<String, Object> processInstanceVariables = new HashMap<>();
        processInstanceVariables.put("selfInspection", 1);
        String processInstanceId = bpmProcessInstanceApi.createProcessInstance(getLoginUserId(), new BpmProcessInstanceCreateReqDTO()
                .setProcessDefinitionKey(PROCESS_KEY).setBusinessKey(updateReqVO.getSheetSchemeId()).setVariables(processInstanceVariables)).getCheckedData();


        // 更新流程ID
        updSheetScheme = new InspectionSheetSchemeDO();
        updSheetScheme.setId(updateReqVO.getSheetSchemeId());
        updSheetScheme.setProcessInstanceId(processInstanceId);
        inspectionSheetSchemeMapper.updateById(updSheetScheme);
    }

    /**
     * 生产操作终端获取任务检测项集合
     * @param reqVO
     * @return
     */
    @Override
    public Map<String, Object> getInspectionSheetRecordList4Terminal(InspectionSheetRecordReqVO reqVO) {

        Map<String, Object> resultMap = new HashMap<>();
        // 检验任务id获取检验产品集合
        List<InspectionSheetSchemeMaterialDO> materialList = inspectionSheetSchemeMaterialMapper.selectList(InspectionSheetSchemeMaterialDO::getInspectionSheetSchemeId, reqVO.getInspectionSchemeId());
        // 过滤等于当前物料条码的产品
        List<InspectionSheetSchemeMaterialDO> list = materialList.stream().filter(o -> StringUtils.isNotBlank(o.getBarCode()) && o.getBarCode().equals(reqVO.getBarCode())).collect(Collectors.toList());
        // 当前条码查询不到对应的产品 查询所有没有barCode的产品
        if(list.size() == 0) {
            materialList = materialList.stream().filter(o -> StringUtils.isBlank(o.getBarCode())).collect(Collectors.toList());
        }
        else {
            materialList = list;
        }
        // 获取自检任务
        InspectionSheetSchemeDO sheetScheme = inspectionSheetSchemeMapper.selectById(reqVO.getInspectionSchemeId());

        // 自检任务
        if(sheetScheme.getSelfInspection() != null){
            // 待自检
            if (ObjUtil.equal(sheetScheme.getSelfAssignmentStatus(), InspectionSelfAssignmentStatusEnum.TOINSPECT_SELF.getStatus())) {
                // 过滤待自检未检验的产品
                materialList = materialList.stream().filter(a -> a.getSelfStatus() == null).collect(Collectors.toList());
            }
            // 待互检
            else if(ObjUtil.equal(sheetScheme.getSelfAssignmentStatus(), InspectionSelfAssignmentStatusEnum.TOINSPECT_MUTUAL.getStatus())){
                materialList = materialList.stream().filter(a -> a.getMutualStatus() == null).collect(Collectors.toList());
            }
            //  专检
            else if (ObjUtil.equal(sheetScheme.getSelfAssignmentStatus(), InspectionSelfAssignmentStatusEnum.TOINSPECT_SPEC.getStatus())) {
                materialList = materialList.stream().filter(a -> a.getSpecStatus() == null).collect(Collectors.toList());
            }
        }
        else {
            // 过滤未检验的产品
            materialList = materialList.stream().filter(a -> a.getStatus() == null).collect(Collectors.toList());
        }

        // 取一个未检测的产品
        if(materialList.size() > 0){
            resultMap.put("schemeMaterialId", materialList.get(0).getId());
            resultMap.put("inspectionResult", materialList.get(0).getInspectionResult());
            resultMap.put("records", inspectionSheetRecordMapper.getInspectionSheetRecordList4Terminal(materialList.get(0).getId()));
        }
        else {
            resultMap.put("records", Collections.emptyList());
        }
        return resultMap;
    }

    /**
     * 生产操作终端产品检验
     * @param updateReqVO
     */
    @Override
    @Transactional
    public void updateInspectionRecordTerminal(InspectionSheetUpdateTerminalReqVO updateReqVO) {

        // barcode查询库存
        MaterialStockRespDTO stockRespDTO = materialStockApi.getMaterialAtLocationByBarCode(updateReqVO.getBarCode()).getData();
        // 验证当前物料是否在当前工位
        if(!StringUtils.isNotBlank(updateReqVO.getDeviceId()) && updateReqVO.getDeviceId().equals(stockRespDTO.getAtLocationId())){
            throw exception(MATERIAL_LOCATION_ERROR);
        }

        updateInspectionRecord(BeanUtils.toBean(updateReqVO, InspectionSheetUpdateReqVO.class));
    }

    /**
     * 检验终端工序检验开始
     * @param updateReqVO
     */
    @Override
    public void updateMcsRecordBegin(InspectionSheetRecordReqVO updateReqVO) {
        InspectionSheetSchemeDO sheetScheme = inspectionSheetSchemeMapper.selectById(updateReqVO.getInspectionSchemeId());
        InspectionSheetDO sheet = inspectionSheetMapper.selectById(sheetScheme.getInspectionSheetId());
        // 单据来源生产
        // 非自检
        if(sheet.getSourceType() == InspectionSheetSourceTypeEnum.PRODUCE.getStatus() && sheetScheme.getSelfInspection()== null){

            McsBatchRecordDTO batchRecordDTO = mcsManufacturingControlApi.getBatchDetailByRecordId(sheet.getRecordId()).getData();
            // 没有任务
            if(batchRecordDTO == null){
                throw exception(500, "当前任务不可开工");
            }

            // 当前任务已开工
            if(batchRecordDTO.getStatus() == MCS_BATCH_RECORD_STATUS_ONGOING ){
                return;
            }

            McsBatchRecordEventDTO batchRecord = new McsBatchRecordEventDTO();
            batchRecord.setBatchRecordId(sheet.getRecordId());
            batchRecord.setBarCode(updateReqVO.getBarCode());
            batchRecord.setOperatorId(getLoginUserId().toString());
            batchRecord.setDeviceUnitId(updateReqVO.getDeviceId());
            // 开工
            CommonResult<?> result = mcsManufacturingControlApi.batchRecordStart(batchRecord);
            if(!result.isSuccess()){
                throw exception(result.getCode(), result.getMsg());
            }
        }
    }

    /**
     * 物料管理模式为单件
     * 验证物料条码不能重复
     */
    private Integer validMaterialBarCode(InspectionSheetSchemeDO sheetScheme, List<InspectionSheetSchemeMaterialDO> materialList, String barCode){
        HashSet<String> set = new HashSet<>();
        set.add(sheetScheme.getMaterialConfigId());
        List<MaterialConfigRespDTO> materialConfigList =  materialMCCApi.getMaterialConfigList(set).getCheckedData();
        // 检验产品为大于1才验证重复
        // 生产检验自检 互检 专检为同一个产品不进行验证
        if (materialList.size() > 1){
            // 物料管理模式
            Integer materialManage = materialConfigList.get(0).getMaterialManage();
            // 单件 验证条码不能重复
            if(materialManage == 1){
                // 过滤出检验完成 且 barcode相同的数据
                List<InspectionSheetSchemeMaterialDO> list = materialList.stream().filter(a -> StringUtils.isNotBlank(a.getBarCode()) && a.getBarCode().equals(barCode) && InspectionSheetSchemeMaterialStatusEnum.FINISH.getStatus().equals(a.getStatus())).collect(Collectors.toList());
                if(list.size() > 0){
                    throw exception(INSPECTION_SHEET_SCHEME_MATERIAL_BAR_CODE_DUPLICATE);
                }
            }
            return materialManage;
        }

        // 生产检验
        // 验证barCode
        if(sheetScheme.getSchemeType() == 2){
            // 条码不同
            if(!barCode.equals(materialList.get(0).getBarCode())){
                throw exception(MATERIAL_STOCK_NOT_EXISTS);
            }
        }

        return null;
    }


    @Override
    public List<InspectionSheetRecordDO> getInspectionSheetRecords(AnalysisReqVO reqVO) {
        return inspectionSheetRecordMapper.getInspectionSheetRecords(reqVO);
    }
}
