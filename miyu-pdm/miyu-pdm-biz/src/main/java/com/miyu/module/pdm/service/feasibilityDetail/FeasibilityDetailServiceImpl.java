package com.miyu.module.pdm.service.feasibilityDetail;

import cn.hutool.core.util.IdUtil;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import cn.iocoder.yudao.framework.security.core.LoginUser;
import cn.iocoder.yudao.framework.security.core.util.SecurityFrameworkUtils;
import cn.iocoder.yudao.module.bpm.api.task.BpmProcessInstanceApi;
import cn.iocoder.yudao.module.bpm.api.task.dto.BpmProcessInstanceCreateReqDTO;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.miyu.module.pdm.controller.admin.feasibilityDetail.vo.*;
import com.miyu.module.pdm.controller.admin.feasibilityTask.vo.FeasibilityTaskReqVO;
import com.miyu.module.pdm.controller.admin.processPlanDetail.vo.ProcedureSchemaItemReqVO;
import com.miyu.module.pdm.controller.admin.processPlanDetail.vo.ProcedureSchemaItemRespVO;
import com.miyu.module.pdm.dal.dataobject.feasibilityDetail.*;
import com.miyu.module.pdm.dal.dataobject.feasibilityTask.FeasibilityTaskDO;
import com.miyu.module.pdm.dal.dataobject.processPlanDetail.ProcedureSchemaItemDO;
import com.miyu.module.pdm.dal.mysql.feasibilityDetail.*;
import com.miyu.module.pdm.dal.mysql.feasibilityTask.FeasibilityTaskMapper;
import com.miyu.module.pdm.enums.FeasibilityTaskStatusEnum;
import com.miyu.module.pdm.enums.PDMAuditStatusEnum;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.annotation.Resource;
import java.util.*;

import static com.miyu.module.pdm.enums.ApiConstants.FEASIBILITY_PROCESS_KEY;

@Service
@Validated
public class FeasibilityDetailServiceImpl implements FeasibilityDetailService {

    @Resource
    private FeasibilityDetailMapper feasibilityDetailMapper;

    @Resource
    private FeasibilityResultMapper feasibilityResultMapper;

    @Resource
    private DemandDeviceMapper demandDeviceMapper;

    @Resource
    private DemandMaterialMapper demandMaterialMapper;

    @Resource
    private DemandCutterMapper demandCutterMapper;

    @Resource
    private DemandHiltMapper demandHiltMapper;

    @Resource
    private QuotaPerPartMapper quotaPerPartMapper;

    @Resource
    private FeasibilityTaskMapper feasibilityTaskMapper;

    @Resource
    private DemandMeasureMapper demandMeasureMapper;

    @Resource
    private ProcedureSchemaItemMapper procedureSchemaItemMapper;

    @Resource
    private BpmProcessInstanceApi bpmProcessInstanceApi;

    @Override
    public List<ProjPartBomRespVO> getProjPartBomListByProjectCode(ProjPartBomReqVO reqVO) {
        String projectCode = reqVO.getProjectCode();
        String partNumber = reqVO.getPartNumber();
        Integer status = reqVO.getStatus();
        LoginUser loginUser = SecurityFrameworkUtils.getLoginUser();
        String reviewedBy = "";
        if(reqVO.getViewSelf()) {//只看我的
            reviewedBy = loginUser.getId().toString();
        }
        return feasibilityDetailMapper.selectPartList(projectCode, partNumber, status, reviewedBy);
    }

    @Override
    public ProjPartBomRespVO getPartDetailByTaskId(String id) {
        return feasibilityDetailMapper.selectPartDetail(id);
    }

    @Override
    public FeasibilityResultDO getFeasibilityResult(FeasibilityResultReqVO reqVO) {
        return feasibilityResultMapper.selectFeasibilityResult(reqVO);
    }

    @Override
    public String createFeasibilityResult(FeasibilityResultSaveReqVO createReqVO) {
        // 插入
        FeasibilityResultDO resultDO = BeanUtils.toBean(createReqVO, FeasibilityResultDO.class)
                .setId(IdUtil.fastSimpleUUID())
                ;
        feasibilityResultMapper.insert(resultDO);
        // 更新技术评估任务状态为2评估中
        return resultDO.getId();
    }

    @Override
    public void updateFeasibilityResult(FeasibilityResultSaveReqVO updateReqVO) {
        // 更新
        FeasibilityResultDO updateObj = BeanUtils.toBean(updateReqVO, FeasibilityResultDO.class);
        feasibilityResultMapper.updateById(updateObj);
        // 更新技术评估任务状态为2评估中
    }

    @Override
    public void saveSelectedResource(ResourceSelectedReqVO reqVO) {
        //选择资源先删后插
        feasibilityDetailMapper.deleteByProjectCode(reqVO);
        List<String> ids = reqVO.getIds();
        ids.forEach(id -> {
            FeasibilityDetailDO detailDO = BeanUtils.toBean(reqVO, FeasibilityDetailDO.class)
                    .setId(IdUtil.fastSimpleUUID())
                    .setResourcesTypeId(id)
                    ;
            feasibilityDetailMapper.insert(detailDO);
        });
    }

    @Override
    public void deleteSelectedDevice(FeasibilityDetailReqVO reqVO) {
        feasibilityDetailMapper.deleteByResourceId(reqVO);
    }

    @Override
    public void updateProcessingTime(FeasibilityDetailReqVO reqVO) {
        LambdaUpdateWrapper<FeasibilityDetailDO> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.set(FeasibilityDetailDO::getProcessingTime, reqVO.getProcessingTime());
        updateWrapper.eq(FeasibilityDetailDO::getProjectCode, reqVO.getProjectCode());
        updateWrapper.eq(FeasibilityDetailDO::getPartVersionId, reqVO.getPartVersionId());
        updateWrapper.eq(FeasibilityDetailDO::getResourcesType, reqVO.getResourcesType());
        updateWrapper.eq(FeasibilityDetailDO::getResourcesTypeId, reqVO.getResourcesTypeId());
        feasibilityDetailMapper.update(updateWrapper);
    }

    @Override
    public List<FeasibilityDetailRespVO> getResourceListByPart(ResourceSelectedReqVO reqVO) {
        List<FeasibilityDetailDO> list = feasibilityDetailMapper.selectResourceList(reqVO);
        return BeanUtils.toBean(list, FeasibilityDetailRespVO.class);
    }

    @Override
    public List<DemandDeviceRespVO> getDemandDeviceList(DemandDeviceReqVO reqVO) {
        List<DemandDeviceDO> list = demandDeviceMapper.selectDemandDeviceList(reqVO);
        return BeanUtils.toBean(list, DemandDeviceRespVO.class);
    }

    @Override
    public String createDemandDevcie(DemandDeviceReqVO reqVO) {
        DemandDeviceDO demandDeviceDO = BeanUtils.toBean(reqVO, DemandDeviceDO.class)
                .setId(IdUtil.fastSimpleUUID());
        demandDeviceMapper.insert(demandDeviceDO);
        return demandDeviceDO.getId();
    }

    @Override
    public void updateDemandDevice(DemandDeviceReqVO reqVO) {
        DemandDeviceDO updateObj = BeanUtils.toBean(reqVO, DemandDeviceDO.class);
        demandDeviceMapper.updateById(updateObj);
    }

    @Override
    public void deleteDemandDevice(String id) {
        demandDeviceMapper.deleteById(id);
    }

    @Override
    public DemandDeviceDO getDemandDevice(String id) {
        return demandDeviceMapper.selectById(id);
    }

    @Override
    public List<DemandMaterialRespVO> getDemandMaterialList(DemandMaterialReqVO reqVO) {
        List<DemandMaterialDO> list = demandMaterialMapper.selectDemandMaterialList(reqVO);
        return BeanUtils.toBean(list, DemandMaterialRespVO.class);
    }

    @Override
    public String createDemandMaterial(DemandMaterialReqVO reqVO) {
        DemandMaterialDO demandMaterialDO = BeanUtils.toBean(reqVO, DemandMaterialDO.class)
                .setId(IdUtil.fastSimpleUUID());
        demandMaterialMapper.insert(demandMaterialDO);
        return demandMaterialDO.getId();
    }

    @Override
    public void updateDemandMaterial(DemandMaterialReqVO reqVO) {
        DemandMaterialDO updateObj = BeanUtils.toBean(reqVO, DemandMaterialDO.class);
        demandMaterialMapper.updateById(updateObj);
    }

    @Override
    public void deleteDemandMaterial(String id) {
        demandMaterialMapper.deleteById(id);
    }

    @Override
    public DemandMaterialDO getDemandMaterial(String id) {
        return demandMaterialMapper.selectById(id);
    }

    @Override
    public List<DemandMeasureRespVO> getDemandMeasureList(DemandMeasureReqVO reqVO) {
        List<DemandMeasureDO> list = demandMeasureMapper.selectDemandMeasureList(reqVO);
        return BeanUtils.toBean(list, DemandMeasureRespVO.class);
    }

    @Override
    public String createDemandMeasure(DemandMeasureReqVO reqVO) {
        DemandMeasureDO demandMeasureDO = BeanUtils.toBean(reqVO, DemandMeasureDO.class)
                .setId(IdUtil.fastSimpleUUID());
        demandMeasureMapper.insert(demandMeasureDO);
        return demandMeasureDO.getId();
    }

    @Override
    public void updateDemandMeasure(DemandMeasureReqVO reqVO) {
        DemandMeasureDO updateObj = BeanUtils.toBean(reqVO, DemandMeasureDO.class);
        demandMeasureMapper.updateById(updateObj);
    }

    @Override
    public void deleteDemandMeasure(String id) {
        demandMeasureMapper.deleteById(id);
    }

    @Override
    public DemandMeasureDO getDemandMeasure(String id) {
        return demandMeasureMapper.selectById(id);
    }

    @Override
    public List<DemandCutterRespVO> getDemandCutterList(DemandCutterReqVO reqVO) {
        List<DemandCutterDO> list = demandCutterMapper.selectDemandCutterList(reqVO);
        return BeanUtils.toBean(list, DemandCutterRespVO.class);
    }

    @Override
    public String createDemandCutter(DemandCutterReqVO reqVO) {
        DemandCutterDO demandCutterDO = BeanUtils.toBean(reqVO, DemandCutterDO.class)
                .setId(IdUtil.fastSimpleUUID());
        demandCutterMapper.insert(demandCutterDO);
        return demandCutterDO.getId();
    }

    @Override
    public void updateDemandCutter(DemandCutterReqVO reqVO) {
        DemandCutterDO updateObj = BeanUtils.toBean(reqVO, DemandCutterDO.class);
        demandCutterMapper.updateById(updateObj);
    }

    @Override
    public void deleteDemandCutter(String id) {
        demandCutterMapper.deleteById(id);
    }

    @Override
    public DemandCutterDO getDemandCutter(String id) {
        return demandCutterMapper.selectById(id);
    }

    @Override
    public List<DemandHiltRespVO> getDemandHiltList(DemandHiltReqVO reqVO) {
        List<DemandHiltDO> list = demandHiltMapper.selectDemandHiltList(reqVO);
        return BeanUtils.toBean(list, DemandHiltRespVO.class);
    }

    @Override
    public String createDemandHilt(DemandHiltReqVO reqVO) {
        DemandHiltDO demandHiltDO = BeanUtils.toBean(reqVO, DemandHiltDO.class)
                .setId(IdUtil.fastSimpleUUID());
        demandHiltMapper.insert(demandHiltDO);
        return demandHiltDO.getId();
    }

    @Override
    public void updateDemandHilt(DemandHiltReqVO reqVO) {
        DemandHiltDO updateObj = BeanUtils.toBean(reqVO, DemandHiltDO.class);
        demandHiltMapper.updateById(updateObj);
    }

    @Override
    public void deleteDemandHilt(String id) {
        demandHiltMapper.deleteById(id);
    }

    @Override
    public DemandHiltDO getDemandHilt(String id) {
        return demandHiltMapper.selectById(id);
    }

    @Override
    public QuotaPerPartDO getQuotaPerPart(QuotaPerPartReqVO reqVO) {
        return quotaPerPartMapper.selectQuotaPerPart(reqVO);
    }

    @Override
    public String createQuotaPerPart(QuotaPerPartSaveReqVO createReqVO) {
        // 插入
        QuotaPerPartDO resultDO = BeanUtils.toBean(createReqVO, QuotaPerPartDO.class)
                .setId(IdUtil.fastSimpleUUID());
        quotaPerPartMapper.insert(resultDO);

        return resultDO.getId();
    }

    @Override
    public void updateQuotaPerPart(QuotaPerPartSaveReqVO updateReqVO) {
        // 更新
        QuotaPerPartDO updateObj = BeanUtils.toBean(updateReqVO, QuotaPerPartDO.class);
        quotaPerPartMapper.updateById(updateObj);
    }

    @Override
    public void updateFeasibilityTaskStatus(FeasibilityTaskReqVO updateReqVO) {
        LambdaUpdateWrapper<FeasibilityTaskDO> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.set(FeasibilityTaskDO::getStatus, updateReqVO.getStatus());
        updateWrapper.eq(FeasibilityTaskDO::getProjectCode, updateReqVO.getProjectCode());
        updateWrapper.eq(FeasibilityTaskDO::getPartVersionId, updateReqVO.getPartVersionId());
        feasibilityTaskMapper.update(updateWrapper);
    }

    @Override
    public void startProcessInstance(FeasibilityTaskReqVO updateReqVO) {
        FeasibilityTaskDO taskDO = BeanUtils.toBean(updateReqVO, FeasibilityTaskDO.class);
        // 1. 创建技术评估审批流程实例
        Map<String, Object> variables = new HashMap<>();
        String processInstanceId = bpmProcessInstanceApi.createProcessInstance(SecurityFrameworkUtils.getLoginUserId(), new BpmProcessInstanceCreateReqDTO()
                .setProcessDefinitionKey(FEASIBILITY_PROCESS_KEY).setBusinessKey(updateReqVO.getId()).setVariables(variables)).getCheckedData();
        // 2. 更新技术评估任务流程实例号及审批状态
        feasibilityTaskMapper.updateById(taskDO.setProcessInstanceId(processInstanceId)
                .setApprovalStatus(PDMAuditStatusEnum.PROCESS.getStatus())
                .setStatus(taskDO.getStatus()));
    }

    @Override
    public void updateProcessInstanceStatus(String id, Integer status) {
        FeasibilityTaskDO taskDO = feasibilityTaskMapper.selectById(id);
        taskDO.setId(id);
        taskDO.setApprovalStatus(status);
        if (PDMAuditStatusEnum.APPROVE.getStatus().equals(status)) {
            //审核通过后 更新任务状态已完成
            taskDO.setStatus(FeasibilityTaskStatusEnum.FINISH.getStatus().toString());
        } else if (PDMAuditStatusEnum.REJECT.getStatus().equals(status)) {
            //审核不通过 更新任务状态审核失败
            taskDO.setStatus(FeasibilityTaskStatusEnum.REJECT.getStatus().toString());
        }
        feasibilityTaskMapper.updateById(taskDO);
    }

    @Override
    public List<ProjPartBomRespVO> getPartListByProjectCodeNew(ProjPartBomReqVO reqVO) {
        String projectCode = reqVO.getProjectCode();
        String partNumber = reqVO.getPartNumber();
        Integer status = reqVO.getStatus();
        LoginUser loginUser = SecurityFrameworkUtils.getLoginUser();
        String reviewedBy = "";
        if(reqVO.getViewSelf()) {//只看我的
            reviewedBy = loginUser.getId().toString();
        }
        return feasibilityDetailMapper.selectPartListNew(projectCode, partNumber, status, reviewedBy, reqVO.getProjectStatus());
    }

    @Override
    public ProjPartBomRespVO getPartDetailByTaskIdNew(String id) {
        return feasibilityDetailMapper.selectPartDetailNew(id);
    }

    @Override
    public void updateProjectstatus(String projectCode) {
        feasibilityTaskMapper.updateProjectstatus(projectCode);
    }

    @Override
    public int deleteMeesage(String projectCode, String partNumber, String partName, String processCondition) {
        return feasibilityTaskMapper.deleteMessage(projectCode, partNumber, partName, processCondition);
    }


}
