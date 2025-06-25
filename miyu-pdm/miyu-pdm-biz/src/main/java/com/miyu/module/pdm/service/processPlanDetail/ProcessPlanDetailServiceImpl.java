package com.miyu.module.pdm.service.processPlanDetail;


import cn.hutool.core.util.IdUtil;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.framework.security.core.LoginUser;
import cn.iocoder.yudao.framework.security.core.util.SecurityFrameworkUtils;
import cn.iocoder.yudao.module.bpm.api.task.BpmProcessInstanceApi;
import cn.iocoder.yudao.module.bpm.api.task.dto.BpmProcessInstanceCreateReqDTO;
import com.alibaba.nacos.shaded.com.google.common.annotations.VisibleForTesting;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.miyu.module.pdm.controller.admin.feasibilityDetail.vo.ProjPartBomReqVO;
import com.miyu.module.pdm.controller.admin.feasibilityDetail.vo.ResourceSelectedReqVO;
import com.miyu.module.pdm.controller.admin.processDetailTask.vo.ProcessDetailTaskReqVO;
import com.miyu.module.pdm.controller.admin.processPlanDetail.vo.*;
import com.miyu.module.pdm.controller.admin.processTask.vo.ProcedureRespVO;
import com.miyu.module.pdm.controller.admin.processTask.vo.ProjPartBomTreeRespVO;
import com.miyu.module.pdm.dal.dataobject.procedure.ProcedureDO;
import com.miyu.module.pdm.dal.dataobject.procedure.ProcedureDetailDO;
import com.miyu.module.pdm.dal.dataobject.procedure.ProcedureFileDO;
import com.miyu.module.pdm.dal.dataobject.processDetailTask.ProcessDetailTaskDO;
import com.miyu.module.pdm.dal.dataobject.processPlanDetail.*;
import com.miyu.module.pdm.dal.dataobject.processVersion.ProcessVersionDO;
import com.miyu.module.pdm.dal.mysql.feasibilityDetail.ProcedureSchemaItemMapper;
import com.miyu.module.pdm.dal.mysql.process.ProcedureDetailMapper;
import com.miyu.module.pdm.dal.mysql.process.ProcedureFileMapper;
import com.miyu.module.pdm.dal.mysql.process.ProcedureMapper;
import com.miyu.module.pdm.dal.mysql.process.ProcessVersionMapper;
import com.miyu.module.pdm.dal.mysql.processDetailTask.ProcessDetailTaskMapper;
import com.miyu.module.pdm.dal.mysql.processPlanDetail.*;
import com.miyu.module.pdm.enums.PDMAuditStatusEnum;
import com.miyu.module.pdm.enums.ProcessDetailTaskStatusEnum;
import com.miyu.module.pdm.enums.ProcessChangeStatusEnum;
import com.mzt.logapi.context.LogRecordContext;
import com.mzt.logapi.starter.annotation.LogRecord;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import javax.annotation.Resource;
import java.util.*;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static com.miyu.module.pdm.enums.ApiConstants.*;
import static com.miyu.module.pdm.enums.ErrorCodeConstants.*;
import static com.miyu.module.pdm.enums.LogRecordConstants.*;

@Service
@Validated
public class ProcessPlanDetailServiceImpl implements ProcessPlanDetailService {

    @Resource
    private ProcessPlanDetailMapper processPlanDetailMapper;

    @Resource
    private ProcedureMapper procedureMapper;

    @Resource
    private StepMapper stepMapper;

    @Resource
    private CustomizedAttributeValMapper customizedAttributeValMapper;

    @Resource
    private ProcedureDetailMapper procedureDetailMapper;

    @Resource
    private StepDetailMapper stepDetailMapper;
    @Resource
    private ProcedureFileMapper procedureFileMapper;
    @Resource
    private StepFileMapper stepFileMapper;

    @Resource
    private NcMapper ncMapper;

    @Resource
    private ProcessVersionNcMapper processVersionNcMapper;
    @Resource
    private BpmProcessInstanceApi bpmProcessInstanceApi;
    @Resource
    private ProcessVersionMapper processVersionMapper;

    @Resource
    private ProcessDetailTaskMapper processDetailTaskMapper;

    @Resource
    private ProcedureSchemaItemMapper procedureSchemaItemMapper;

    @Resource
    private ProcessChangeMapper processChangeMapper;

    @Resource
    private ProcessChangeDetailMapper processChangeDetailMapper;

    private void validateStepNameUnique(String id, String procedureId,String stepNum) {
        StepDO step = stepMapper.selectByProcedureIDAndNumAndName(procedureId,stepNum);
        if(step == null){
            return;
        }
        if(id == null ){
            throw exception(STEP_NAME_DUPLICATE);
        }
        if(!Objects.equals(step.getId(),id)){
            throw exception(STEP_NAME_DUPLICATE);
        }
    }
    private void updateStepValidate(String id, String procedureId,String stepNum) {
        StepDO step = stepMapper.updateByProcedureIDAndNumAndName(procedureId,stepNum);
        if(step == null){
            return;
        }
        if(id == null ){
            throw exception(STEP_NAME_DUPLICATE);
        }
        if(!Objects.equals(step.getId(),id)){
            throw exception(STEP_NAME_DUPLICATE);
        }
    }

    @VisibleForTesting
    void validateProductNumberUnique(String id, String procedureNumber, String processVersionId) {
        //首先不管是新增还是修改 首先都要判断procedure是否为空 如果为空直接返回空
        if (StringUtils.isBlank(procedureNumber)) {
            return;
        }
            List<ProcedureDO> procedures = procedureMapper.selectByProcedureNumAndProcessVersionId(procedureNumber, processVersionId);
            if (StringUtils.isNotBlank(id)) {
                ProcedureDO existingProcedure = procedureMapper.selectById(id);
                if (existingProcedure == null || !procedures.isEmpty() && !procedures.get(0).getId().equals(id)) {
                    throw exception(PRODURENUMBER_ISNOT_EXISTS);
                }
            } else if (!procedures.isEmpty()) {
                throw exception(PRODURENUMBER_EXISTS);
            }
    }


    // 根据工艺规程版本id获取工序列表的（接口用）
    @Override
    public List<ProcessPlanDetailRespVO> getProcedureListByProcessVersionId(ProcessPlanDetailReqVO reqVO) {
        String processVersionId = reqVO.getProcessVersionId();
        return procedureMapper.selectByProcedureListByProcessVersionId(processVersionId);
    }

    //查询工艺更改单列表
    @Override
    public List<ProcessChangeRespVO> getChangeOrderList(ProcessChangeReqVO reqVO) {
        String projectCode = reqVO.getProjectCode();
        String processVersionId = reqVO.getProcessVersionId();
        Integer status = reqVO.getStatus();
        return processChangeMapper.selectChangeOrderList(projectCode,processVersionId,status);
    }

    @Override
    public List<ProcessChangeRespVO> getChangeDetailList(String processChangeId) {
        return processChangeMapper.selectChangeDetailList(processChangeId);
    }

    @Override
    public ProcessChangeRespVO getProcessChangeById(String id) {
        return processChangeMapper.selectProcessChangeById(id);
    }

    @Override
    public List<ProcessChangeRespVO> getProcessChangeDetailById(ProcessChangeReqVO reqVO) {
        String id = reqVO.getId();
        return processChangeMapper.selectProcessChangeDetailList(id);
    }


    @Override
    public List<ProcessChangeRespVO> getChangeDetailItem(ProcessChangeReqVO reqVO) {
        return processChangeMapper.getChangeDetailItemAll(reqVO);
    }

    @Override
    public String saveOrderItem(ProcessChangeReqVO b) {
        String projectCode = b.getProjectCode();
        String processVersionId = b.getProcessVersionId();
        String changeCode = b.getChangeCode();
        // 查询数据库中相同项目号，工艺规程下是否存在要新增的changeCode
        ProcessChangeDO before = processChangeMapper.selectNeed(projectCode, processVersionId, changeCode);
        // 检查 before 是否为空,为空执行新增
        if (before == null) {
            // 创建新的记录列表
            ProcessChangeDO itemDO = BeanUtils.toBean(b, ProcessChangeDO.class)
                    .setId(IdUtil.fastSimpleUUID());
            processChangeMapper.insert(itemDO);
        } else if (before.getChangeCode().equals(b.getChangeCode())) {
            throw exception(ORDER_CHANGE_IS_EXISTS);
        }
        return "success";
    }

    @Override
    public void updateOrderItem(ProcessChangeReqVO b) {
        String projectCode = b.getProjectCode();
        String processVersionId = b.getProcessVersionId();
        String changeCode = b.getChangeCode();
        // 查询数据库中相同项目号，工艺规程下是否存在要新增的changeCode
        ProcessChangeDO before = processChangeMapper.selectNeed(projectCode, processVersionId, changeCode);
        if(before != null && !before.getId().equals(b.getId()) && before.getChangeCode().equals(b.getChangeCode())) {
            throw exception(ORDER_CHANGE_IS_EXISTS);
        }
        // 更新
        ProcessChangeDO updateObj = BeanUtils.toBean(b, ProcessChangeDO.class);
        processChangeMapper.updateById(updateObj);
    }

    @Override
    public void deleteOrderItem(String id) {
        processChangeMapper.deleteById(id);
    }

    @Override
    public ProcessChangeDO getChangeOrderById(String id) {
        return processChangeMapper.selectById(id);
    }

    @Override
    public void deleteChangeOrderById(String id) {
        processChangeMapper.deleteById(id);
    }

    @Override
    public void deleteOrderDetailById(String id) {
        processChangeDetailMapper.deleteChangeOrderById(id);
    }



    @Override
    public void startProcessChangeInstance(ProcessChangeReqVO updateReqVO) {
        ProcessChangeDO changeDO = BeanUtils.toBean(updateReqVO,  ProcessChangeDO.class);
        // 1. 创建技术评估审批流程实例
        Map<String, Object> variables = new HashMap<>();
        String OrderChangeInstanceId = bpmProcessInstanceApi.createProcessInstance(SecurityFrameworkUtils.getLoginUserId(), new BpmProcessInstanceCreateReqDTO()
                .setProcessDefinitionKey(PROCESS_CHANGE_KEY).setBusinessKey(updateReqVO.getId()).setVariables(variables)).getCheckedData();
        // 2. 更新技术评估任务流程实例号及审批状态
        processChangeMapper.updateById(changeDO.setProcessInstanceId(OrderChangeInstanceId)
                .setApprovalStatus(ProcessChangeStatusEnum.PROCESS.getStatus())
                .setStatus(changeDO.getStatus()));
    }

    @Override
    public void updateProcessChangeInstanceStatus(String id, Integer status) {
        ProcessChangeDO changeDO =  processChangeMapper.selectById(id);
        changeDO.setId(id);
        changeDO.setApprovalStatus(status);
        if (PDMAuditStatusEnum.APPROVE.getStatus().equals(status)) {
            //审核通过后 更新任务状态已完成
            changeDO.setStatus(ProcessChangeStatusEnum.FINISH.getStatus());
        } else if (PDMAuditStatusEnum.REJECT.getStatus().equals(status)) {
            //审核不通过 更新任务状态审核失败
            changeDO.setStatus(ProcessChangeStatusEnum.REJECT.getStatus());
        }
        processChangeMapper.updateById(changeDO);
    }

    //数控程序
    @Override
    public List<NcRespVO> getNc(ProcessVersionNcReqVO req1VO) {
        String processVersionId = req1VO.getProcessVersionId();
        String procedureId = req1VO.getProcedureId();
        String stepId = req1VO.getStepId();
        return ncMapper.getNcByIds(stepId,procedureId,processVersionId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveNc(NcReqVO reqVO, ProcessVersionNcReqVO req1VO) {
        String id = IdUtil.fastSimpleUUID();
        NcDO ncDO = BeanUtils.toBean(reqVO, NcDO.class)
                .setId(id);
        ProcessVersionNcDO processVersionNcDO = BeanUtils.toBean(req1VO, ProcessVersionNcDO.class)
                .setNcId(id);
        ncMapper.insert(ncDO);
        processVersionNcMapper.insert(processVersionNcDO);
    }

    @Override
    public void deleteNc(NcReqVO reqVO, ProcessVersionNcReqVO req1VO) {
        ncMapper.deleteNcById(reqVO);
        processVersionNcMapper.deleteNcByNcId(req1VO);
    }

    //添加工步
    @Override
    public String addStep(ProcessPlanDetailReqVO addReqVO) {
        validateStepNameUnique(null,addReqVO.getProcedureId(),addReqVO.getStepNum());
        // 更改为前端生成id
        StepDO step= BeanUtils.toBean(addReqVO,StepDO.class);
        stepMapper.insert(step);
        // 处理工步自定义属性
        List<CustomizedAttributeValReqVO> list = addReqVO.getAttributeValList();
        if(list != null && list.size() > 0) {
            for(CustomizedAttributeValReqVO attr : list) {
                CustomizedAttributeValDO attrDO = new CustomizedAttributeValDO();
                attrDO.setId(IdUtil.fastSimpleUUID());
                attrDO.setObjectId(step.getId());
                attrDO.setAttributeId(attr.getId());
                attrDO.setAttributeValue(attr.getAttrDefaultValue());
                customizedAttributeValMapper.insert(attrDO);
            }
        }
        return step.getId();
    }

    //更新工步
    @Override
    @LogRecord(type = PDM_PROCESS_PLAN_TYPE, subType = PDM_PROCESS_STEP_UPDATE_SUB_TYPE, bizNo = "{{#updateReqVO.id}}",
            success = PDM_PROCESS_STEP_UPDATE_SUCCESS)
    public void updateStep(ProcessPlanDetailReqVO updateReqVO) {
        // 获取”新工步序号“数据库中的 step 记录
        StepDO beforeStep = stepMapper.selectById(updateReqVO.getId());
        // 判断 stepNum 是否发生变化
        boolean isStepNumChanged = !Objects.equals(beforeStep.getStepNum(), updateReqVO.getStepNum());
        // 如果 stepNum 发生变化，进行唯一性验证
        if (isStepNumChanged) {
            updateStepValidate(updateReqVO.getId(), updateReqVO.getProcedureId(), updateReqVO.getStepNum());
        }
        StepDO updateObj = BeanUtils.toBean(updateReqVO, StepDO.class);
        stepMapper.updateById(updateObj);
        // 处理工步自定义属性
        List<CustomizedAttributeValReqVO> list = updateReqVO.getAttributeValList();
        if(list != null && list.size() > 0) {
            // 先删除后插入
            customizedAttributeValMapper.delete(new LambdaQueryWrapper<CustomizedAttributeValDO>()
                    .eq(CustomizedAttributeValDO::getObjectId, updateObj.getId()));
            for(CustomizedAttributeValReqVO attr : list) {
                CustomizedAttributeValDO attrDO = new CustomizedAttributeValDO();
                attrDO.setId(IdUtil.fastSimpleUUID());
                attrDO.setObjectId(updateObj.getId());
                attrDO.setAttributeId(attr.getId());
                attrDO.setAttributeValue(attr.getAttrDefaultValue());
                customizedAttributeValMapper.insert(attrDO);
            }
        }
        // 记录日志
        LogRecordContext.putVariable("stepName", updateReqVO.getStepName());
    }

    //删除工步
    @Override
    @LogRecord(type = PDM_PROCESS_PLAN_TYPE, subType = PDM_PROCESS_STEP_DELETE_SUB_TYPE, bizNo = "{{#id}}",
            success = PDM_PROCESS_STEP_DELETE_SUCCESS)
    public void deleteStep(String id) {
        stepMapper.deleteById(id);
        // 删除工步自定义属性
        customizedAttributeValMapper.delete(new LambdaQueryWrapper<CustomizedAttributeValDO>()
                .eq(CustomizedAttributeValDO::getObjectId, id));
        StepDO stepDO = stepMapper.selectById(id);
        // 记录日志
        LogRecordContext.putVariable("stepName", stepDO.getStepName());
    }
    //添加工序
    @Override
    public String addProcedure(ProcessPlanDetailReqVO addReqVO) {
        validateProductNumberUnique(null, addReqVO.getProcedureNum(),addReqVO.getProcessVersionId());
        ProcedureDO procedure= BeanUtils.toBean(addReqVO,ProcedureDO.class);
        procedureMapper.insert(procedure);
        return procedure.getId();
    }

    //更新工序
    @Override
    @LogRecord(type = PDM_PROCESS_PLAN_TYPE, subType = PDM_PROCESS_PROCEDURE_UPDATE_SUB_TYPE, bizNo = "{{#updateReqVO.id}}",
            success = PDM_PROCESS_PROCEDURE_UPDATE_SUCCESS)
    public void updateProcedure(ProcessPlanDetailReqVO updateReqVO) {
        validateProductNumberUnique(updateReqVO.getId(), updateReqVO.getProcedureNum(), updateReqVO.getProcessVersionId());
        ProcedureDO updateObj = BeanUtils.toBean(updateReqVO, ProcedureDO.class);
        procedureMapper.updateById(updateObj);
        // 记录日志
        LogRecordContext.putVariable("procedureName", updateReqVO.getProcedureName());
    }
    //删除工序
    @Override
    @LogRecord(type = PDM_PROCESS_PLAN_TYPE, subType = PDM_PROCESS_PROCEDURE_DELETE_SUB_TYPE, bizNo = "{{#id}}",
            success = PDM_PROCESS_PROCEDURE_DELETE_SUCCESS)
    public void deleteProcedure(String id) {
        // 校验是否有下属工步
        int count = Integer.parseInt(stepMapper.selectCountByProcedureId(id));
        if (count > 0) {
            throw exception(STEP_EXITS_CHILDREN);
        }
        procedureMapper.deleteById(id);
        ProcedureDO procedureDO = procedureMapper.selectById(id);
        // 记录日志
        LogRecordContext.putVariable("procedureName", procedureDO.getProcedureName());
    }


    @Override
    public List<ProcessPlanDetailRespVO> getPartListByProcessPlanDetailId(String projectCode) {
        List<ProcessPlanDetailDO> list=processPlanDetailMapper.getPartListByProcessPlanDetailId(projectCode);
        return BeanUtils.toBean(list,ProcessPlanDetailRespVO.class);
    }
    @Override
    public List<ProjPartBomTreeRespVO> getProjPartBomTreeList(ProjPartBomReqVO reqVO) {
        String projectCode = reqVO.getProjectCode();
        String partNumber = reqVO.getPartNumber();
        Integer status = reqVO.getStatus();
        LoginUser loginUser = SecurityFrameworkUtils.getLoginUser();
        String reviewedBy = "";
        if (reqVO.getViewSelf() != null && reqVO.getViewSelf()) {//只看我的
            reviewedBy = loginUser.getId().toString();
        }
        return processPlanDetailMapper.selectPartTreeList(projectCode, partNumber, status, reviewedBy);
    }
    @Override
    public List<ProjPartBomTreeRespVO> getProjPartBomTreeByTaskId(ProjPartBomReqVO reqVO) {
        return processPlanDetailMapper.selectPartTreeListByTaskId(reqVO.getTaskId());
    }
    @Override
    public List<ProcessPlanDetailRespVO> selectProcessPlanDetailList(ProcessPlanDetailReqVO reqVO) {
        String projectCode = reqVO.getProjectCode();
        String partVersionId = reqVO.getPartVersionId();
        String status = reqVO.getStatus();
        return processPlanDetailMapper.selectProcessPlanDetailList(projectCode,partVersionId,status);
    }

    @Override
    public ProcessPlanDetailRespVO getProcessPlanDetail(String id) {
        List<ProcessPlanDetailRespVO> list = processPlanDetailMapper.selectProcessDetail(id);
        if(list.size()>0) {//同一零件同一本工艺规程会关联多个项目，取出一条
            return list.get(0);
        }
        return null;
    }

    //获取树列表
    @Override
    public List<ProcessPlanDetailRespVO> getProcessPlanDetailTreeList(ProcessPlanDetailReqVO reqVO) {
        String processVersionId = reqVO.getProcessVersionId();
        return processPlanDetailMapper.selectProcessPlanDetailTreeList(processVersionId);
    }


    @Override
    public ProcessPlanDetailRespVO getProcess(String id) {
        return processPlanDetailMapper.selectProcessByPartVersionId(id);
    }

    @Override
    public ProcedureRespVO getProcedure(String id) {
        ProcedureDO procedure = procedureMapper.selectById(id);
        ProcedureRespVO procedureRespVO = BeanUtils.toBean(procedure,ProcedureRespVO.class);
        return procedureRespVO;
    }

//    @Override
//    public ProcessPlanDetailRespVO getStep(String id) {
//        return processPlanDetailMapper.selectStep(id);
//    }

    @Override
    public StepRespVO getStep(String id) {
        StepDO step = stepMapper.selectById(id);
        StepRespVO stepRespVO = BeanUtils.toBean(step,StepRespVO.class);
        return stepRespVO;
    }

    @Override
    public List<ProcedureDetailRespVO> getResourceListByProcedure(ResourceSelectedReqVO reqVO) {
        List<ProcedureDetailDO> list = procedureDetailMapper.selectResourceList(reqVO);
        return BeanUtils.toBean(list, ProcedureDetailRespVO.class);
    }

    @Override
    public void deleteSelectedDeviceProcedure(ProcedureDetailReqVO reqVO) {
        procedureDetailMapper.deleteByResourceId(reqVO);
    }

    @Override
    public void saveSelectedResourceProcedure(ResourceSelectedReqVO reqVO) {
        procedureDetailMapper.deleteByProjectCode(reqVO);
        List<String> ids = reqVO.getIds();
        ids.forEach(id -> {
            ProcedureDetailDO detailDO = BeanUtils.toBean(reqVO, ProcedureDetailDO.class)
                    .setId(IdUtil.fastSimpleUUID())
                    .setResourcesTypeId(id);
            procedureDetailMapper.insert(detailDO);
        });
    }

    @Override
    public List<StepDetailRespVO> getResourceListByStep(ResourceSelectedReqVO reqVO) {
        List<StepDetailDO> list = stepDetailMapper.selectResourceList(reqVO);
        return BeanUtils.toBean(list, StepDetailRespVO.class);
    }

    @Override
    public void deleteSelectedDeviceStep(StepDetailReqVO reqVO) {
        stepDetailMapper.deleteByResourceId(reqVO);
    }

    @Override
    public void saveSelectedResourceStep(ResourceSelectedReqVO reqVO) {
        stepDetailMapper.deleteByProjectCode(reqVO);
        List<String> ids = reqVO.getIds();
        ids.forEach(id -> {
            StepDetailDO detailDO = BeanUtils.toBean(reqVO, StepDetailDO.class)
                    .setId(IdUtil.fastSimpleUUID())
                    .setResourcesTypeId(id)
                    ;
            stepDetailMapper.insert(detailDO);
        });
    }

    @Override
    public List<ProcedureFileRespVO> getProcedureFiles(ProcedureFileSaveReqVO reqVO) {
        String processVersionId = reqVO.getProcessVersionId();
        String procedureId = reqVO.getProcedureId();
        return procedureFileMapper.getProcedureFilesByIds(processVersionId,procedureId);
    }

    @Override
    public ProcedureFileDO getProcedureFileById(String id) {
        return procedureFileMapper.selectById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveSelectedResource(ProcedureFileSaveReqVO reqVO) {
        if(reqVO.getId() != null && !reqVO.getId().equals("")) {
            ProcedureFileDO updateObj = BeanUtils.toBean(reqVO, ProcedureFileDO.class);
            procedureFileMapper.updateById(updateObj);
        } else {
            ProcedureFileDO processDetailDO = BeanUtils.toBean(reqVO, ProcedureFileDO.class)
                    .setId(IdUtil.fastSimpleUUID());
            procedureFileMapper.insert(processDetailDO);
        }
    }

    @Override
    public void deleteSelectedResource(String id) {
        procedureFileMapper.deleteById(id);

    }

    @Override
    public void startProcessInstance(ProcessDetailTaskReqVO updateReqVO) {
        ProcessDetailTaskDO taskDO = BeanUtils.toBean(updateReqVO, ProcessDetailTaskDO.class);
        // 1. 创建工艺详细设计审批流程实例(工序)
        Map<String, Object> variables = new HashMap<>();
        String processInstanceId = bpmProcessInstanceApi.createProcessInstance(SecurityFrameworkUtils.getLoginUserId(), new BpmProcessInstanceCreateReqDTO()
                .setProcessDefinitionKey(PROCESS_PLAN_DETAIL_PROCESS_KEY).setBusinessKey(updateReqVO.getId()).setVariables(variables)).getCheckedData();
        // 2. 更新工艺详细设计任务流程实例号及审批状态
        processDetailTaskMapper.updateById(taskDO.setProcessInstanceId(processInstanceId)
                .setApprovalStatus(PDMAuditStatusEnum.PROCESS.getStatus())
                .setStatus(taskDO.getStatus()));
    }

    @Override
    public void updateProcessInstanceStatus(String id, Integer status) {
        ProcessDetailTaskDO taskDO = processDetailTaskMapper.selectById(id);
        taskDO.setId(id);
        taskDO.setApprovalStatus(status);
        if (PDMAuditStatusEnum.APPROVE.getStatus().equals(status)) {
            //审核通过后 更新任务状态已完成
            taskDO.setStatus(ProcessDetailTaskStatusEnum.FINISH.getStatus().toString());
        } else if (PDMAuditStatusEnum.REJECT.getStatus().equals(status)) {
            //审核不通过 更新任务状态审核失败'
            taskDO.setStatus(ProcessDetailTaskStatusEnum.REJECT.getStatus().toString());
        }
        processDetailTaskMapper.updateById(taskDO);
        if (PDMAuditStatusEnum.APPROVE.getStatus().equals(status)) {
            //工序审核通过后 查工艺规程下工序是否全部走完审批 若是，更新工艺规程状态已定版
            Long taskCount = processDetailTaskMapper.selectCount(new LambdaQueryWrapperX<ProcessDetailTaskDO>()
                    .eq(ProcessDetailTaskDO::getProjectCode, taskDO.getProjectCode())
                    .eq(ProcessDetailTaskDO::getPartVersionId, taskDO.getPartVersionId())
                    .eq(ProcessDetailTaskDO::getProcessVersionId, taskDO.getProcessVersionId())
                    .eq(ProcessDetailTaskDO::getStatus, 5));
            Long procedureCount = procedureMapper.selectCount(new LambdaQueryWrapperX<ProcedureDO>()
                    .eq(ProcedureDO::getProcessVersionId, taskDO.getProcessVersionId())
                    .orderByDesc(ProcedureDO::getUpdateTime));
            if(taskCount == procedureCount) {
                ProcessVersionDO processVersionDO = processVersionMapper.selectById(taskDO.getProcessVersionId());
                processVersionDO.setStatus(3);
                processVersionMapper.updateById(processVersionDO);
            }
        }
    }

    @Override
    public void saveSelectedStepFile(StepFileSaveReqVO reqVO) {
        StepFileDO stepFileDO = BeanUtils.toBean(reqVO, StepFileDO.class)
                .setId(IdUtil.fastSimpleUUID());
        stepFileMapper.insert(stepFileDO);
    }

    @Override
    public List<StepFileRespVO> getStepFiles(StepFileSaveReqVO reqVO) {
        String processVersionId = reqVO.getProcessVersionId();
        String procedureId = reqVO.getProcedureId();
        String stepId = reqVO.getStepId();
        return stepFileMapper.getStepFilesByIds(processVersionId,procedureId,stepId);
    }

    @Override
    public void deleteSelectedStepFile(String id) {
        stepFileMapper.deleteById(id);
    }

    //实现类
    @Override
    public List<ProcessPlanDetailRespVO> getProcessPlanList(ProcessPlanDetailRespVO reqVO) {
        String partNumber = reqVO.getPartNumber();
        return processPlanDetailMapper.getProcessPlanList(partNumber);
    }

    @Override
    public void updateProcessDetailTaskStatus(ProcessDetailTaskReqVO updateReqVO) {
        LambdaUpdateWrapper<ProcessDetailTaskDO> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.set(ProcessDetailTaskDO::getStatus, updateReqVO.getStatus());
        updateWrapper.eq(ProcessDetailTaskDO::getProjectCode, updateReqVO.getProjectCode());
        updateWrapper.eq(ProcessDetailTaskDO::getPartVersionId, updateReqVO.getPartVersionId());
        updateWrapper.eq(ProcessDetailTaskDO::getProcessVersionId, updateReqVO.getProcessVersionId());
        updateWrapper.eq(ProcessDetailTaskDO::getProcedureId, updateReqVO.getProcedureId());
        processDetailTaskMapper.update(updateWrapper);
    }

    @Override
    public void updateCutternum(StepDetailReqVO reqVO) {
            LambdaUpdateWrapper<StepDetailDO> updateWrapper = new LambdaUpdateWrapper<>();
            updateWrapper.set(StepDetailDO::getCutternum, reqVO.getCutternum());
            updateWrapper.eq(StepDetailDO::getProcessVersionId, reqVO.getProcessVersionId());
            updateWrapper.eq(StepDetailDO::getProcedureId, reqVO.getProcedureId());
            updateWrapper.eq(StepDetailDO::getStepId, reqVO.getStepId());
            updateWrapper.eq(StepDetailDO::getResourcesType, reqVO.getResourcesType());
            updateWrapper.eq(StepDetailDO::getResourcesTypeId, reqVO.getResourcesTypeId());
            stepDetailMapper.update(updateWrapper);
    }

    @Override
    public List<ProcedureSchemaItemRespVO> getProcedureSchemaItemList(ProcedureSchemaItemReqVO reqVO) {
        String procedureVersionId = reqVO.getProcessVersionId();
        String procedureId = reqVO.getProcedureId();
        return procedureSchemaItemMapper.getProcedureSchemaItemByIds(procedureVersionId,procedureId);
    }

    @Override
    public String saveProcedureSchemaItem(List<ProcedureSchemaItemReqVO> createReqVO) {
        //保存时 可以一次存多个 用for 循环 并且将procedureId,procedureVersionId 存到 procedureSchemaItemDO中
        // 先删除后插入
        if(createReqVO != null && createReqVO.size() > 0) {
            ProcedureSchemaItemReqVO req = createReqVO.get(0);
            procedureSchemaItemMapper.deleteByVersionAndProcedure(req.getProcessVersionId(), req.getProcedureId());
        }
        // 创建一个存储数据库对象的列表
        List<ProcedureSchemaItemDO> itemDOS = new ArrayList<>();

        // 遍历请求列表
        for (ProcedureSchemaItemReqVO reqVO : createReqVO) {
            // 创建数据库对象
            ProcedureSchemaItemDO itemDO = new ProcedureSchemaItemDO();
            itemDO.setId(IdUtil.fastSimpleUUID());
            // 设置具体字段
            itemDO.setProcedureId(reqVO.getProcedureId());
            itemDO.setProcessVersionId(reqVO.getProcessVersionId());
            itemDO.setInspectionItemId(reqVO.getInspectionItemId());
            itemDO.setNumber(reqVO.getNumber());
            itemDO.setReferenceType(reqVO.getReferenceType());
            itemDO.setMaxValue(reqVO.getMaxValue());
            itemDO.setMinValue(reqVO.getMinValue());
            itemDO.setContent(reqVO.getContent());
            itemDO.setJudgement(reqVO.getJudgement());
            itemDO.setAcceptanceQualityLimit(reqVO.getAcceptanceQualityLimit());
            // 添加到列表
            itemDOS.add(itemDO);
        }
        // 批量保存到数据库
        procedureSchemaItemMapper.insertBatch(itemDOS);
        return "success";
    }

    @Override
    public void updateProcedureSchemaItem(ProcedureSchemaItemReqVO updateReqVO) {
        ProcedureSchemaItemDO procedureSchemaItemDO = BeanUtils.toBean(updateReqVO, ProcedureSchemaItemDO.class);
        procedureSchemaItemMapper.updateById(procedureSchemaItemDO);
    }

    @Override
    public void deleteProcedureSchemaItem(String id) {
        procedureSchemaItemMapper.deleteById(id);
    }

    @Override
    public ProcedureSchemaItemDO getProcedureSchemaItem(String id) {
        return null;
    }

    @Override
    public Long getStepCountByCategoryId(Long categoryId) {
        return stepMapper.selectCountByCategoryId(categoryId);
    }

    @Override
    public List<CustomizedAttributeValRespVO> getStepAttributeValList(String objectId) {
        List<CustomizedAttributeValDO> valList = customizedAttributeValMapper.selectList(new LambdaQueryWrapperX<CustomizedAttributeValDO>()
                .eq(CustomizedAttributeValDO::getObjectId, objectId));
        return BeanUtils.toBean(valList, CustomizedAttributeValRespVO.class);
    }
}
