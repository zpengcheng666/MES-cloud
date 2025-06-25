package com.miyu.module.pdm.service.processSupplement;

import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import cn.iocoder.yudao.framework.security.core.util.SecurityFrameworkUtils;
import cn.iocoder.yudao.module.bpm.api.task.BpmProcessInstanceApi;
import cn.iocoder.yudao.module.bpm.api.task.dto.BpmProcessInstanceCreateReqDTO;
import com.google.common.annotations.VisibleForTesting;
import com.miyu.module.pdm.controller.admin.processPlanDetail.vo.ProcessPlanDetailReqVO;
import com.miyu.module.pdm.controller.admin.processPlanDetail.vo.ProcessPlanDetailRespVO;
import com.miyu.module.pdm.controller.admin.processSupplement.vo.ProcessSupplementPageReqVO;
import com.miyu.module.pdm.controller.admin.processSupplement.vo.ProcessSupplementSaveReqVO;
import com.miyu.module.pdm.dal.dataobject.processSupplement.ProcessSupplementDO;
import com.miyu.module.pdm.dal.mysql.processSupplement.ProcessSupplementMapper;
import com.miyu.module.pdm.enums.PDMAuditStatusEnum;
import com.miyu.module.pdm.enums.ProcessSupplementStatusEnum;
import com.mzt.logapi.context.LogRecordContext;
import com.mzt.logapi.starter.annotation.LogRecord;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static com.miyu.module.pdm.enums.ApiConstants.PROCESS_SUPPLEMENT_PROCESS_KEY;
import static com.miyu.module.pdm.enums.ErrorCodeConstants.*;
import static com.miyu.module.pdm.enums.LogRecordConstants.*;

@Service
@Slf4j
public class ProcessSupplementServiceImpl implements ProcessSupplementService {

    @Resource
    private ProcessSupplementMapper processSupplementMapper;

    @Resource
    private BpmProcessInstanceApi bpmProcessInstanceApi;

    @Override
    public String createProcessSupplement(ProcessSupplementSaveReqVO createReqVO) {
        // 检验补加工工艺规程编号唯一
        validateProcessSupplementNameUnique(null, createReqVO.getProcessCodeSupplement());
        ProcessSupplementDO ProcessSupplement= BeanUtils.toBean(createReqVO,ProcessSupplementDO.class);
        processSupplementMapper.insert(ProcessSupplement);
        return ProcessSupplement.getId();
    }

    @Override
    @LogRecord(type = PDM_PROCESS_SUPPLEMENT_TYPE, subType = PDM_PROCESS_SUPPLEMENT_UPDATE_SUB_TYPE, bizNo = "{{#updateReqVO.id}}",
            success = PDM_PROCESS_SUPPLEMENT_UPDATE_SUCCESS)
    public void updateProcessSupplement(ProcessSupplementSaveReqVO updateReqVO) {
        // 校验存在
        validateProcessSupplementExists(updateReqVO.getId());
        // 检验补加工工艺规程编号唯一
        validateProcessSupplementNameUnique(updateReqVO.getId(), updateReqVO.getProcessCodeSupplement());
        // 更新
        ProcessSupplementDO updateObj = BeanUtils.toBean(updateReqVO, ProcessSupplementDO.class);
        processSupplementMapper.updateById(updateObj);
        // 记录日志
        LogRecordContext.putVariable("processCode", updateReqVO.getProcessCodeSupplement());
    }

    /**
     * 检验补加工工艺规程编号是否唯一
     */
    @VisibleForTesting
    void validateProcessSupplementNameUnique(String id, String processCodeSupplement) {
        if (StrUtil.isBlank(processCodeSupplement)) {
            return;
        }
        ProcessSupplementDO supplementDO = processSupplementMapper.selectByProcessCodeSupplement(processCodeSupplement);
        if (supplementDO == null) {
            return;
        }
        // 如果 id 为空，说明不用比较是否为相同 id
        if (id == null || id.equals("")) {
            throw exception(PROCESSSUPPLEMENT_EXISTS);
        }
        if (!supplementDO.getId().equals(id)) {
            throw exception(PROCESSSUPPLEMENT_EXISTS);
        }
    }

    private void validateProcessSupplementExists(String id) {
        if (processSupplementMapper.selectById(id) == null) {
            throw exception(PROCESSSUPPLEMENT_NOT_EXISTS);
        }
    }
    @Override
    @LogRecord(type = PDM_PROCESS_SUPPLEMENT_TYPE, subType = PDM_PROCESS_SUPPLEMENT_DELETE_SUB_TYPE, bizNo = "{{#id}}",
            success = PDM_PROCESS_SUPPLEMENT_DELETE_SUCCESS)
    public void deleteProcessSupplement(String id) {
        // 校验存在
        validateProcessSupplementExists(id);
        // 删除
        processSupplementMapper.deleteById(id);
        ProcessSupplementDO supplementDO = processSupplementMapper.selectById(id);
        // 记录日志
        LogRecordContext.putVariable("processCode", supplementDO.getProcessCodeSupplement());
    }

    @Override
    public ProcessSupplementDO getProcessSupplement(String id) {
        return processSupplementMapper.selectById(id);
    }

    @Override
    public PageResult<ProcessSupplementDO> getProcessSupplementPage(ProcessSupplementPageReqVO reqVO) {
        return processSupplementMapper.selectPage(reqVO);
    }

    @Override
    public List<ProcessPlanDetailRespVO> getProcessSupplementTreeList(ProcessPlanDetailReqVO reqVO) {
        String processVersionId = reqVO.getProcessVersionId();
        return processSupplementMapper.selectProcessSupplementTreeList(processVersionId);
    }

    @Override
    public void startProcessInstance(ProcessSupplementSaveReqVO reqVO) {
        ProcessSupplementDO supplementDO = BeanUtils.toBean(reqVO, ProcessSupplementDO.class);
        // 1. 创建补加工工艺规程审批流程实例
        Map<String, Object> variables = new HashMap<>();
        String processInstanceId = bpmProcessInstanceApi.createProcessInstance(SecurityFrameworkUtils.getLoginUserId(), new BpmProcessInstanceCreateReqDTO()
                .setProcessDefinitionKey(PROCESS_SUPPLEMENT_PROCESS_KEY).setBusinessKey(reqVO.getId()).setVariables(variables)).getCheckedData();
        // 2. 更新补加工工艺规程流程实例号及审批状态
        processSupplementMapper.updateById(supplementDO.setProcessInstanceId(processInstanceId)
                .setApprovalStatus(PDMAuditStatusEnum.PROCESS.getStatus())
                .setStatus(supplementDO.getStatus()));
    }

    @Override
    public void updateProcessSupplementInstanceStatus(String id, Integer status) {
        ProcessSupplementDO supplementDO =  processSupplementMapper.selectById(id);
        supplementDO.setId(id);
        supplementDO.setApprovalStatus(status);
        if (PDMAuditStatusEnum.APPROVE.getStatus().equals(status)) {
            //审核通过后 更新任务状态已完成
            supplementDO.setStatus(ProcessSupplementStatusEnum.FINISH.getStatus().toString());
        } else if (PDMAuditStatusEnum.REJECT.getStatus().equals(status)) {
            //审核不通过 更新任务状态审核失败
            supplementDO.setStatus(ProcessSupplementStatusEnum.REJECT.getStatus().toString());
        }
        processSupplementMapper.updateById(supplementDO);
    }
}
