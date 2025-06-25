package com.miyu.module.pdm.service.process;

import cn.hutool.core.util.IdUtil;
import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.framework.security.core.util.SecurityFrameworkUtils;
import cn.iocoder.yudao.module.bpm.api.task.BpmProcessInstanceApi;
import cn.iocoder.yudao.module.bpm.api.task.dto.BpmProcessInstanceCreateReqDTO;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.miyu.module.pdm.controller.admin.processRoute.vo.ProcessRouteListReqVO;
import com.miyu.module.pdm.controller.admin.processTask.vo.ProcessRespVO;
import com.miyu.module.pdm.controller.admin.processTask.vo.ProcessSaveReqVO;
import com.miyu.module.pdm.controller.admin.processTask.vo.ProcessSelectedReqVO;
import com.miyu.module.pdm.controller.admin.processTask.vo.ProcessTaskReqVO;
import com.miyu.module.pdm.dal.dataobject.procedure.ProcedureDO;
import com.miyu.module.pdm.dal.dataobject.procedure.ProcedureDetailDO;
import com.miyu.module.pdm.dal.dataobject.procedure.ProcedureFileDO;
import com.miyu.module.pdm.dal.dataobject.process.ProcessDO;
import com.miyu.module.pdm.dal.dataobject.processPlanDetail.*;
import com.miyu.module.pdm.dal.dataobject.processRoute.ProcessRouteDO;
import com.miyu.module.pdm.dal.dataobject.processTask.PbomProcessVersionDO;
import com.miyu.module.pdm.dal.dataobject.processTask.ProcessTaskDO;
import com.miyu.module.pdm.dal.dataobject.processVersion.ProcessVersionDO;
import com.miyu.module.pdm.dal.mysql.process.*;
import com.miyu.module.pdm.dal.mysql.processPlanDetail.*;
import com.miyu.module.pdm.dal.mysql.processRoute.ProcessRouteMapper;
import com.miyu.module.pdm.dal.mysql.processTask.ProcessTaskMapper;
import com.miyu.module.pdm.enums.PDMAuditStatusEnum;
import com.miyu.module.pdm.enums.ProcessTaskStatusEnum;
import com.miyu.module.qms.api.dto.InspectionSchemeRespDTO;
import com.miyu.module.qms.api.dto.InspectionSchemeSaveReqDTO;
import com.miyu.module.qms.api.inspectionscheme.InspectionSchemeApi;
import com.miyu.module.qms.enums.InspectionSchemeEffectiveStatusEnum;
import com.mzt.logapi.context.LogRecordContext;
import com.mzt.logapi.starter.annotation.LogRecord;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.*;

import static com.miyu.module.pdm.enums.ApiConstants.PROCESS_PLAN_PROCESS_KEY;
import static com.miyu.module.pdm.enums.LogRecordConstants.*;


@Service
public class processServiceImpl implements processService {
    @Resource
    private ProcessMapper processMapper;
    @Resource
    private ProcessVersionMapper processVersionMapper;
    @Resource
    private ProcessRouteMapper processRouteMapper;
    @Resource
    private ProcedureMapper procedureMapper;
    @Resource
    private PbomProcessVersionMapper pbomProcessVersionMapper;
    @Resource
    private ProcedureDetailMapper procedureDetailMapper;
    @Resource
    private ProcedureFileMapper procedureFileMapper;
    @Resource
    private StepMapper stepMapper;
    @Resource
    private StepDetailMapper stepDetailMapper;
    @Resource
    private ProcessVersionNcMapper processVersionNcMapper;
    @Resource
    private BpmProcessInstanceApi bpmProcessInstanceApi;
    @Resource
    private ProcessTaskMapper processTaskMapper;
    @Resource
    private CustomizedAttributeValMapper customizedAttributeValMapper;
    @Resource
    private InspectionSchemeApi inspectionSchemeApi;

    public ProcessRespVO selectProcessById(String id) {
        return processMapper.selectCappProcessById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String createProcess(ProcessSaveReqVO createReqVO) {
        // 创建并保存ProcessDO对象
        ProcessDO resultDO = BeanUtils.toBean(createReqVO, ProcessDO.class);
        // 处理加工方案码01~99
        // 查询零件工艺规程最大版本号、最大加工方案码
        Map<String, Object> map = processVersionMapper.selectProcessVersion(createReqVO.getPartVersionId(), "", createReqVO.getProjectCode());
        String num = "1";
        if(map==null){//未查询出工艺方案时默认为01
            num="1";
        }else{
            //工艺方案码
            String code = map.get("process_scheme_code").toString();
            int planNum = Integer.parseInt(code);
            planNum++;
            num = planNum+"";
        }
        num = String.format("%0" + 2 + "d", Integer.parseInt(num));
        resultDO.setProcessSchemeCode(num);
        processMapper.insert(resultDO);

        // 创建并保存ProcessVersionDO对象
        ProcessVersionDO processVersionDO = BeanUtils.toBean(createReqVO, ProcessVersionDO.class);
        processVersionDO.setProcessId(resultDO.getId());
        processVersionMapper.insert(processVersionDO);

        String[] processRouteNames = resultDO.getProcessRouteName().split("-");
        for (int i = 0; i < processRouteNames.length; i++) {
            // 工序号规则：05 10 15...
            // 工步号规则：10.01 10.02...
            String procedureNum = "05";
            if(i != 0) {
                procedureNum = String.valueOf((i+1) * 5);
            }
            String processRouteName = processRouteNames[i];
            ProcedureDO procedureDO = new ProcedureDO()
                    .setProcessVersionId(processVersionDO.getId())
                    .setProcedureNum(procedureNum)
                    .setProcedureName(processRouteName);
            procedureMapper.insert(procedureDO);
        }

        // 创建并保存PbomProcessVersionDO对象
        PbomProcessVersionDO pbomProcessVersionDO = new PbomProcessVersionDO()
                .setId(IdUtil.fastSimpleUUID())
                .setProjectCode(createReqVO.getProjectCode())
                .setPartVersionId(createReqVO.getPartVersionId())
                .setProcessTaskId(createReqVO.getProcessTaskId())
//                .setProjPartBomId(createReqVO.getProjPartBomId())
                .setProcessVersionId(processVersionDO.getId());
        pbomProcessVersionMapper.insert(pbomProcessVersionDO);

        // 返回ProcessDO的ID
        return resultDO.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @LogRecord(type = PDM_PROCESS_PLAN_TYPE, subType = PDM_PROCESS_PLAN_UPDATE_SUB_TYPE, bizNo = "{{#updateReqVO.id}}",
            success = PDM_PROCESS_PLAN_UPDATE_SUCCESS)
    public void updateProcess(ProcessSaveReqVO updateReqVO) {
        // 获取ProcessVersionDO
        ProcessVersionDO processVersionDO = BeanUtils.toBean(updateReqVO, ProcessVersionDO.class);
        processVersionMapper.updateById(processVersionDO);

        ProcessDO resultDO = BeanUtils.toBean(updateReqVO, ProcessDO.class)
                .setId(processVersionDO.getProcessId());
        // 更新ProcessDO
        processMapper.updateById(resultDO);
        // 记录日志
        LogRecordContext.putVariable("processCode", updateReqVO.getProcessCode());
    }

    @Override
    public List<ProcessRouteDO> getRouteList(ProcessRouteListReqVO listReqVO) {
        return processRouteMapper.selectList(listReqVO);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @LogRecord(type = PDM_PROCESS_PLAN_TYPE, subType = PDM_PROCESS_PLAN_DELETE_SUB_TYPE, bizNo = "{{#deleteReqVO.processVersionId}}",
            success = PDM_PROCESS_PLAN_DELETE_SUCCESS)
    public void deleteProcess(ProcessSaveReqVO deleteReqVO) {
        ProcessVersionDO processVersionDO = processVersionMapper.selectById(deleteReqVO.getProcessVersionId());
        //工艺规程来源(1新建 2已定版 3升版)
        if(deleteReqVO.getSource() == 1) {
            processMapper.deleteById(processVersionDO.getProcessId());
            procedureMapper.delete(new LambdaQueryWrapper<ProcedureDO>()
                    .eq(ProcedureDO::getProcessVersionId, processVersionDO.getId()));
            pbomProcessVersionMapper.delete(new LambdaQueryWrapper<PbomProcessVersionDO>()
                    .eq(PbomProcessVersionDO::getProcessVersionId, deleteReqVO.getProcessVersionId())
//                    .eq(PbomProcessVersionDO::getProjPartBomId, deleteReqVO.getProjPartBomId()));
                    .eq(PbomProcessVersionDO::getProjectCode, deleteReqVO.getProjectCode()));
            processVersionMapper.deleteById(deleteReqVO.getProcessVersionId());
            procedureDetailMapper.delete(new LambdaQueryWrapper<ProcedureDetailDO>()
                    .eq(ProcedureDetailDO::getProcessVersionId, deleteReqVO.getProcessVersionId()));
            procedureFileMapper.delete(new LambdaQueryWrapper<ProcedureFileDO>()
                    .eq(ProcedureFileDO::getProcessVersionId, deleteReqVO.getProcessVersionId()));
            inspectionSchemeApi.deleteInspectionSchemeByTechnologyId(deleteReqVO.getProcessVersionId());
        } else if(deleteReqVO.getSource() == 2) {//已定版工艺规程只需删除pbom关联表
            pbomProcessVersionMapper.delete(new LambdaQueryWrapper<PbomProcessVersionDO>()
                    .eq(PbomProcessVersionDO::getProcessVersionId, deleteReqVO.getProcessVersionId())
//                    .eq(PbomProcessVersionDO::getProjPartBomId, deleteReqVO.getProjPartBomId()));
                    .eq(PbomProcessVersionDO::getProjectCode, deleteReqVO.getProjectCode()));
        } else if(deleteReqVO.getSource() == 3) {//升版的工艺规程需多删除关联表
            procedureMapper.delete(new LambdaQueryWrapper<ProcedureDO>()
                    .eq(ProcedureDO::getProcessVersionId, processVersionDO.getId()));
            pbomProcessVersionMapper.delete(new LambdaQueryWrapper<PbomProcessVersionDO>()
                    .eq(PbomProcessVersionDO::getProcessVersionId, deleteReqVO.getProcessVersionId())
//                    .eq(PbomProcessVersionDO::getProjPartBomId, deleteReqVO.getProjPartBomId()));
                    .eq(PbomProcessVersionDO::getProjectCode, deleteReqVO.getProjectCode()));
            processVersionMapper.deleteById(deleteReqVO.getProcessVersionId());
            //升版工艺规程需多删除相关表
            procedureDetailMapper.delete(new LambdaQueryWrapper<ProcedureDetailDO>()
                    .eq(ProcedureDetailDO::getProcessVersionId, deleteReqVO.getProcessVersionId()));
            procedureFileMapper.delete(new LambdaQueryWrapper<ProcedureFileDO>()
                    .eq(ProcedureFileDO::getProcessVersionId, deleteReqVO.getProcessVersionId()));
            inspectionSchemeApi.deleteInspectionSchemeByTechnologyId(deleteReqVO.getProcessVersionId());
            stepMapper.delete(new LambdaQueryWrapper<StepDO>()
                    .eq(StepDO::getProcessVersionId, processVersionDO.getId()));
            stepDetailMapper.delete(new LambdaQueryWrapper<StepDetailDO>()
                    .eq(StepDetailDO::getProcessVersionId, deleteReqVO.getProcessVersionId()));
            processVersionNcMapper.delete(new LambdaQueryWrapper<ProcessVersionNcDO>()
                    .eq(ProcessVersionNcDO::getProcessVersionId, deleteReqVO.getProcessVersionId()));
        }
        // 记录日志
        LogRecordContext.putVariable("processCode", deleteReqVO.getProcessCode());
    }

    @Override
    public Integer getProcessCountByPartVersionId(String partVersionId,String processCondition) {
        return processVersionMapper.selectProcessCount(partVersionId,processCondition);
    }

    @Override
    public List<ProcessRespVO> getProcessListByPartVersionId(String partVersionId,String processCondition) {
        return processVersionMapper.selectProcessList(partVersionId,processCondition);
    }

    @Override
    public void saveSelectedProcess(ProcessSelectedReqVO reqVO) {
        // 先删后插项目与工艺规程关联表
        pbomProcessVersionMapper.delete(Wrappers.lambdaUpdate(PbomProcessVersionDO.class)
                .eq(PbomProcessVersionDO::getProjectCode, reqVO.getProjectCode())
                .eq(PbomProcessVersionDO::getPartVersionId, reqVO.getPartVersionId())
                .eq(PbomProcessVersionDO::getProcessTaskId, reqVO.getProcessTaskId())
//                .eq(PbomProcessVersionDO::getProjPartBomId, reqVO.getProjPartBomId())
                .eq(PbomProcessVersionDO::getProcessVersionId, reqVO.getProcessVersionId()));
        PbomProcessVersionDO pbomProcessVersionDO = new PbomProcessVersionDO()
                .setId(IdUtil.fastSimpleUUID())
                .setProjectCode(reqVO.getProjectCode())
                .setPartVersionId(reqVO.getPartVersionId())
                .setProcessTaskId(reqVO.getProcessTaskId())
//                .setProjPartBomId(reqVO.getProjPartBomId())
                .setProcessVersionId(reqVO.getProcessVersionId())
                .setSource(2);//选择已定版
        pbomProcessVersionMapper.insert(pbomProcessVersionDO);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveProcessUp(ProcessSelectedReqVO reqVO) {
        //1、生成新版工艺规程
        // 查询零件工艺规程最大版本号
        String nextVersion = "";
        Map<String, Object> map = processVersionMapper.selectProcessVersion(reqVO.getPartVersionId(), reqVO.getProcessId(), "");
        if(map.get("process_version") == null  || map.get("process_version").equals("") ){
            nextVersion="A";
        } else {
            char version1=map.get("process_version").toString().charAt(0);
            int intVersion1=version1;
            int intVersion2=intVersion1+1;
            char version2=(char) intVersion2;
            nextVersion= String.valueOf(version2);
        }
        System.out.println(nextVersion);
        ProcessVersionDO processVersionDO = processVersionMapper.selectById(reqVO.getProcessVersionId());
        ProcessVersionDO processVersionDONew = BeanUtils.toBean(processVersionDO, ProcessVersionDO.class);
        processVersionDONew.setId(IdUtil.fastSimpleUUID());
        processVersionDONew.setSourceId(reqVO.getProcessVersionId());
        processVersionDONew.setProcessVersion(nextVersion);
        processVersionDONew.setStatus(0);
        processVersionDONew.setProcessChangeId(reqVO.getProcessChangeId());
        processVersionMapper.insert(processVersionDONew);
        ProcessDO processDO = processMapper.selectById(processVersionDO.getProcessId());
        // 新版工艺规程版本id
        String processVersionIdNew = processVersionDONew.getId();

        //2、处理项目与工艺规程关联表
        // 先删除项目与旧版工艺规程关联
        pbomProcessVersionMapper.delete(new LambdaQueryWrapper<PbomProcessVersionDO>()
                .eq(PbomProcessVersionDO::getProcessVersionId, reqVO.getProcessVersionId())
                .eq(PbomProcessVersionDO::getProjectCode, reqVO.getProjectCode()));
        PbomProcessVersionDO pbomProcessVersionDO = new PbomProcessVersionDO()
                .setId(IdUtil.fastSimpleUUID())
                .setProjectCode(reqVO.getProjectCode())
                .setPartVersionId(reqVO.getPartVersionId())
                .setProcessTaskId(reqVO.getProcessTaskId())
//                .setProjPartBomId(reqVO.getProjPartBomId())
                .setProcessVersionId(processVersionIdNew)
                .setSource(3);//选择升版
        pbomProcessVersionMapper.insert(pbomProcessVersionDO);

        //3、处理工艺规程与工序关联表
        List<ProcedureDO> procedureDOList = procedureMapper.selectList(new LambdaQueryWrapperX<ProcedureDO>()
                .eq(ProcedureDO::getProcessVersionId, reqVO.getProcessVersionId()));
        // 新版工艺规程关联工序列表
        List<ProcedureDO> procedureDOListNew = new ArrayList<ProcedureDO>();
        for(ProcedureDO procedureDO : procedureDOList) {
            ProcedureDO procedureDONew = BeanUtils.toBean(procedureDO, ProcedureDO.class);
            procedureDONew.setId(IdUtil.fastSimpleUUID());
            procedureDONew.setProcessVersionId(processVersionIdNew);
            procedureMapper.insert(procedureDONew);
            // 源工序id加入新工序列表中，方便复制工序关联信息
            procedureDONew.setProcedureIdSource(procedureDO.getId());
            procedureDOListNew.add(procedureDONew);
        }
        for(ProcedureDO procedureDONew : procedureDOListNew) {
            //3.1、处理工序与制造资源关联表
            List<ProcedureDetailDO> procedureDetailDOList = procedureDetailMapper.selectList(new LambdaQueryWrapperX<ProcedureDetailDO>()
                    .eq(ProcedureDetailDO::getProcessVersionId, reqVO.getProcessVersionId())
                    .eq(ProcedureDetailDO::getProcedureId, procedureDONew.getProcedureIdSource()));
            for(ProcedureDetailDO procedureDetailDO : procedureDetailDOList) {
                ProcedureDetailDO procedureDetailDONew = BeanUtils.toBean(procedureDetailDO, ProcedureDetailDO.class);
                procedureDetailDONew.setId(IdUtil.fastSimpleUUID());
                procedureDetailDONew.setProcessVersionId(processVersionIdNew);
                procedureDetailDONew.setProcedureId(procedureDONew.getId());
                procedureDetailMapper.insert(procedureDetailDONew);
            }
            //3.2、处理工序与草图关联表
            List<ProcedureFileDO> procedureFileDOList = procedureFileMapper.selectList(new LambdaQueryWrapperX<ProcedureFileDO>()
                    .eq(ProcedureFileDO::getProcessVersionId, reqVO.getProcessVersionId())
                    .eq(ProcedureFileDO::getProcedureId, procedureDONew.getProcedureIdSource()));
            for(ProcedureFileDO procedureFileDO : procedureFileDOList) {
                ProcedureFileDO procedureFileDONew = BeanUtils.toBean(procedureFileDO, ProcedureFileDO.class);
                procedureFileDONew.setId(IdUtil.fastSimpleUUID());
                procedureFileDONew.setProcessVersionId(processVersionIdNew);
                procedureFileDONew.setProcedureId(procedureDONew.getId());
                procedureFileMapper.insert(procedureFileDONew);
            }
            //3.3、处理工序与检测项关联表-更改调用QMS
            CommonResult<InspectionSchemeRespDTO> inspectionSchemeDto = inspectionSchemeApi.getInspectionSchemeByProcessId(reqVO.getProcessVersionId(), procedureDONew.getProcedureIdSource());
            if(inspectionSchemeDto != null && inspectionSchemeDto.getData() != null) {
                if(inspectionSchemeDto.getData().getItems() != null && inspectionSchemeDto.getData().getItems().size() > 0) {
                    inspectionSchemeDto.getData().getItems().forEach(o -> o.setId(null));
                }
                InspectionSchemeSaveReqDTO inspectionSchemeDtoNew = BeanUtils.toBean(inspectionSchemeDto.getData(), InspectionSchemeSaveReqDTO.class);
                inspectionSchemeDtoNew.setId(null);
                inspectionSchemeDtoNew.setTechnologyId(processVersionIdNew);
                inspectionSchemeDtoNew.setProcessId(procedureDONew.getId());
                inspectionSchemeDtoNew.setSchemeName(processVersionDO.getProcessName()+"-"+processVersionDONew.getProcessVersion()+"-"+procedureDONew.getProcedureName());
                inspectionSchemeDtoNew.setSchemeNo(processDO.getProcessCode()+"-"+processVersionDONew.getProcessVersion()+"-"+procedureDONew.getProcedureNum());
                inspectionSchemeDtoNew.setIsEffective(InspectionSchemeEffectiveStatusEnum.NOEFFECTIVE.getStatus());
                inspectionSchemeApi.createInspectionScheme(inspectionSchemeDtoNew);
            }
        }

        //4、处理工艺规程与工步关联表
        // 新版工艺规程关联工步列表
        List<StepDO> stepDOListNew = new ArrayList<StepDO>();
        for(ProcedureDO procedureDONew : procedureDOListNew) {
            List<StepDO> stepDOList = stepMapper.selectList(new LambdaQueryWrapperX<StepDO>()
                    .eq(StepDO::getProcessVersionId, reqVO.getProcessVersionId())
                    .eq(StepDO::getProcedureId, procedureDONew.getProcedureIdSource()));
            for(StepDO stepDO : stepDOList) {
                StepDO stepDONew = BeanUtils.toBean(stepDO, StepDO.class);
                stepDONew.setId(IdUtil.fastSimpleUUID());
                stepDONew.setProcessVersionId(processVersionIdNew);
                stepDONew.setProcedureId(procedureDONew.getId());
                stepMapper.insert(stepDONew);
                // 源工序id加入新工步列表中，方便复制工步关联信息
                stepDONew.setProcedureIdSource(stepDO.getProcedureId());
                // 源工步id加入新工步列表中，方便复制工步关联信息
                stepDONew.setStepIdSource(stepDO.getId());
                stepDOListNew.add(stepDONew);
            }
        }
        for(StepDO stepDONew : stepDOListNew) {
            //4.1、处理工步与制造资源关联表
            List<StepDetailDO> stepDetailDOList = stepDetailMapper.selectList(new LambdaQueryWrapperX<StepDetailDO>()
                    .eq(StepDetailDO::getProcessVersionId, reqVO.getProcessVersionId())
                    .eq(StepDetailDO::getProcedureId, stepDONew.getProcedureIdSource())
                    .eq(StepDetailDO::getStepId, stepDONew.getStepIdSource()));
            for(StepDetailDO stepDetailDO : stepDetailDOList) {
                StepDetailDO stepDetailDONew = BeanUtils.toBean(stepDetailDO, StepDetailDO.class);
                stepDetailDONew.setId(IdUtil.fastSimpleUUID());
                stepDetailDONew.setProcessVersionId(processVersionIdNew);
                stepDetailDONew.setProcedureId(stepDONew.getProcedureId());
                stepDetailDONew.setStepId(stepDONew.getId());
                stepDetailMapper.insert(stepDetailDONew);
            }
            //4.2、处理工步与数控程序关联表
            List<ProcessVersionNcDO> processVersionNcDOList = processVersionNcMapper.selectList(new LambdaQueryWrapperX<ProcessVersionNcDO>()
                    .eq(ProcessVersionNcDO::getProcessVersionId, reqVO.getProcessVersionId())
                    .eq(ProcessVersionNcDO::getProcedureId, stepDONew.getProcedureIdSource())
                    .eq(ProcessVersionNcDO::getStepId, stepDONew.getStepIdSource()));
            for(ProcessVersionNcDO processVersionNcDO : processVersionNcDOList) {
                ProcessVersionNcDO processVersionNcDONew = BeanUtils.toBean(processVersionNcDO, ProcessVersionNcDO.class);
                processVersionNcDONew.setId(IdUtil.fastSimpleUUID());
                processVersionNcDONew.setProcessVersionId(processVersionIdNew);
                processVersionNcDONew.setProcedureId(stepDONew.getProcedureId());
                processVersionNcDONew.setStepId(stepDONew.getId());
                processVersionNcMapper.insert(processVersionNcDONew);
            }
            //4.3、处理工步与自定义属性关联表
            List<CustomizedAttributeValDO> attrValueDOList = customizedAttributeValMapper.selectList(new LambdaQueryWrapperX<CustomizedAttributeValDO>()
                    .eq(CustomizedAttributeValDO::getObjectId, stepDONew.getStepIdSource()));
            for(CustomizedAttributeValDO customizedAttributeValDO : attrValueDOList) {
                CustomizedAttributeValDO customizedAttributeValDONew = BeanUtils.toBean(customizedAttributeValDO, CustomizedAttributeValDO.class);
                customizedAttributeValDONew.setId(IdUtil.fastSimpleUUID());
                customizedAttributeValDONew.setObjectId(stepDONew.getId());
                customizedAttributeValMapper.insert(customizedAttributeValDONew);
            }
        }
    }

    @Override
    public void startProcessInstance(ProcessTaskReqVO updateReqVO) {
        ProcessTaskDO taskDO = BeanUtils.toBean(updateReqVO, ProcessTaskDO.class);
        // 1. 创建工艺方案审批流程实例
        Map<String, Object> variables = new HashMap<>();
        String processInstanceId = bpmProcessInstanceApi.createProcessInstance(SecurityFrameworkUtils.getLoginUserId(), new BpmProcessInstanceCreateReqDTO()
                .setProcessDefinitionKey(PROCESS_PLAN_PROCESS_KEY).setBusinessKey(updateReqVO.getId()).setVariables(variables)).getCheckedData();
        // 2. 更新工艺任务流程实例号及审批状态
        processTaskMapper.updateById(taskDO.setProcessInstanceId(processInstanceId)
                .setApprovalStatus(PDMAuditStatusEnum.PROCESS.getStatus())
                .setStatus(taskDO.getStatus()));
    }

    @Override
    public void updateProcessInstanceStatus(String id, Integer status) {
        ProcessTaskDO taskDO = processTaskMapper.selectById(id);
        taskDO.setId(id);
        taskDO.setApprovalStatus(status);
        if (PDMAuditStatusEnum.APPROVE.getStatus().equals(status)) {
            //审核通过后 更新任务状态已完成
            taskDO.setStatus(ProcessTaskStatusEnum.FINISH.getStatus().toString());
        } else if (PDMAuditStatusEnum.REJECT.getStatus().equals(status)) {
            //审核不通过 更新任务状态审核失败
            taskDO.setStatus(ProcessTaskStatusEnum.REJECT.getStatus().toString());
        }
        taskDO.setUpdateTime(LocalDateTime.now());
        processTaskMapper.updateById(taskDO);
        if (PDMAuditStatusEnum.APPROVE.getStatus().equals(status)) {
            //工艺方案审核通过后 更新工艺规程状态已定版(一个零件节点下可能存在多个工艺方案)
            List<PbomProcessVersionDO> pbomProcessVersionDOList = pbomProcessVersionMapper.selectList(new LambdaQueryWrapperX<PbomProcessVersionDO>()
                    .eq(PbomProcessVersionDO::getProjectCode, taskDO.getProjectCode())
                    .eq(PbomProcessVersionDO::getPartVersionId, taskDO.getPartVersionId())
                    .eq(PbomProcessVersionDO::getProcessTaskId, taskDO.getId()));
            for(PbomProcessVersionDO pbomProcessVersionDO : pbomProcessVersionDOList) {
                ProcessVersionDO processVersionDO = processVersionMapper.selectById(pbomProcessVersionDO.getProcessVersionId());
                processVersionDO.setStatus(3);
                processVersionDO.setUpdateTime(LocalDateTime.now());
                processVersionMapper.updateById(processVersionDO);
                //工艺规程定版审批后，更新检验方案发布状态
                inspectionSchemeApi.submitEffective(pbomProcessVersionDO.getProcessVersionId(), InspectionSchemeEffectiveStatusEnum.EFFECTIVE.getStatus());
            }
        }
    }

}