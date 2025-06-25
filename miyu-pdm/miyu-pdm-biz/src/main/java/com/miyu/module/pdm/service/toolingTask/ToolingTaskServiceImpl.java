package com.miyu.module.pdm.service.toolingTask;

import cn.hutool.core.util.IdUtil;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import cn.iocoder.yudao.framework.security.core.util.SecurityFrameworkUtils;
import cn.iocoder.yudao.module.bpm.api.task.BpmProcessInstanceApi;
import cn.iocoder.yudao.module.bpm.api.task.dto.BpmProcessInstanceCreateReqDTO;
import com.miyu.module.pdm.controller.admin.processTask.vo.ProcessTaskReqVO;
import com.miyu.module.pdm.controller.admin.toolingTask.vo.ToolingTaskReqVO;
import com.miyu.module.pdm.controller.admin.toolingTask.vo.ToolingTaskRespVO;
import com.miyu.module.pdm.dal.dataobject.toolingApply.ToolingApplyDO;
import com.miyu.module.pdm.dal.dataobject.toolingTask.ToolingTaskDO;
import com.miyu.module.pdm.dal.mysql.toolingTask.ToolingTaskMapper;
import com.miyu.module.pdm.enums.PDMAuditStatusEnum;
import com.miyu.module.pdm.enums.ToolingApplyStatusEnum;
import com.miyu.module.pdm.enums.ToolingDetailStatusEnum;
import org.springframework.stereotype.Service;

import org.springframework.validation.annotation.Validated;

import javax.annotation.Resource;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.miyu.module.pdm.enums.ApiConstants.TOOLING_DETAIL_KEY;


/**
 * 工装申请信息 Service 实现类
 *
 * @author 上海弥彧
 */
@Service
@Validated
public class ToolingTaskServiceImpl implements ToolingTaskService {

    @Resource
    private ToolingTaskMapper toolingTaskMapper;
    @Resource
    private BpmProcessInstanceApi bpmProcessInstanceApi;
    @Override
    public List<ToolingTaskDO> getToolingTaskList(ToolingTaskReqVO reqVO) {
        return toolingTaskMapper.selectList(reqVO);
    }

    @Override
    public ToolingTaskDO getToolingTask(String id) {
        return toolingTaskMapper.selectById(id);
    }

    @Override
    public List<ToolingTaskRespVO> getToolingTaskDataList(ToolingTaskReqVO reqVO) {
        String toolingCode = reqVO.getToolingCode();
        String toolingNumber = reqVO.getToolingNumber();
        String toolingName = reqVO.getToolingName();
        String status=reqVO.getStatus();
        return toolingTaskMapper.selectToolingTaskList(toolingCode,toolingNumber,toolingName,status);
    }

    @Override
    public List<ToolingTaskRespVO> getToolingDetailList(ToolingTaskReqVO reqVO) {
        String toolingCode = reqVO.getToolingCode();
        String toolingNumber = reqVO.getToolingNumber();
        String toolingName = reqVO.getToolingName();
        String status=reqVO.getStatus();
        return toolingTaskMapper.selectToolingDetailList(toolingCode,toolingNumber,toolingName,status);
    }

    @Override
    public ToolingTaskRespVO getToolingDetailById(String id) {
        return toolingTaskMapper.selectToolingDetailById(id);
    }

    @Override
    public String addToolingTask(ToolingTaskReqVO addReqVO) {
        List<String> toolingApplyIdArr = addReqVO.getToolingApplyIdArr();
        toolingApplyIdArr.forEach(toolingApplyId -> {
            addReqVO.setToolingApplyId(toolingApplyId);
            ToolingTaskDO toolingTaskDO = BeanUtils.toBean(addReqVO, ToolingTaskDO.class)
            .setId(IdUtil.fastSimpleUUID());
            toolingTaskMapper.insert(toolingTaskDO);
        });
        return addReqVO.getId();
}

    @Override
    public void updateToolingTask(ToolingTaskReqVO updateReqVO) {
        ToolingTaskDO updateObj = BeanUtils.toBean(updateReqVO, ToolingTaskDO.class);
//        updateObj.setId(updateReqVO.getTid());
        toolingTaskMapper.updateById(updateObj);
    }
    @Override
    public void startDetailInstance(ToolingTaskReqVO updateReqVO) {
        ToolingTaskDO taskDO= BeanUtils.toBean(updateReqVO, ToolingTaskDO.class);
        // 1. 创建技术评估审批流程实例
        Map<String, Object> variables = new HashMap<>();
        String toolingDetailInstanceId = bpmProcessInstanceApi.createProcessInstance(SecurityFrameworkUtils.getLoginUserId(), new BpmProcessInstanceCreateReqDTO()
                .setProcessDefinitionKey(TOOLING_DETAIL_KEY).setBusinessKey(updateReqVO.getId()).setVariables(variables)).getCheckedData();
        // 2. 更新技术评估任务流程实例号及审批状态
        toolingTaskMapper.updateById(taskDO.setProcessInstanceId(toolingDetailInstanceId)
                .setApprovalStatus(PDMAuditStatusEnum.PROCESS.getStatus())
                .setStatus(taskDO.getStatus()));
    }

    @Override
    public void updateApplyInstanceStatus(String id, Integer status) {
        ToolingTaskDO taskDO = toolingTaskMapper.selectById(id);
        taskDO.setId(id);
        taskDO.setApprovalStatus(status);
        if (PDMAuditStatusEnum.APPROVE.getStatus().equals(status)) {
            //审核通过后 更新任务状态已完成
            taskDO.setStatus(ToolingDetailStatusEnum.FINISH.getStatus());
        } else if (PDMAuditStatusEnum.REJECT.getStatus().equals(status)) {
            //审核不通过 更新任务状态审核失败
            taskDO.setStatus(ToolingDetailStatusEnum.REJECT.getStatus());
        }
        toolingTaskMapper.updateById(taskDO);
    }

    @Override
    public ToolingTaskReqVO getToolingDetail(String id) {
        return toolingTaskMapper.selectById1(id);
    }

    @Override
    public List<ToolingTaskRespVO>  getFileByCustomizedIndex(String customizedIndex) {
        String TableName_DF = "pdm_"+customizedIndex+"_document_file";
        return toolingTaskMapper.selectFileByCustomizedIndex(TableName_DF);
    }
}