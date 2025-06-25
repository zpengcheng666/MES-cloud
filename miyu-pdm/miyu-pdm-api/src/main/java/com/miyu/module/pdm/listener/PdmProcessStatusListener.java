package com.miyu.module.pdm.listener;

import cn.iocoder.yudao.module.bpm.event.BpmProcessInstanceStatusEvent;
import cn.iocoder.yudao.module.bpm.event.BpmProcessInstanceStatusEventListener;
import com.miyu.module.pdm.api.processPlan.ProcessPlanApi;
import com.miyu.module.pdm.enums.ApiConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 工艺方案审批的监听器实现类
 *
 * @author Liuy
 */
@Component
@Slf4j
public class PdmProcessStatusListener extends BpmProcessInstanceStatusEventListener {

    @Resource
    private ProcessPlanApi processPlanApi;

    @Override
    protected String getProcessDefinitionKey() {
        return ApiConstants.PROCESS_PLAN_PROCESS_KEY;
    }

    @Override
    protected void onEvent(BpmProcessInstanceStatusEvent event) {
        log.info("审批回调成功");
        //更新审批状态
        processPlanApi.updateProcessTaskStatus(event.getBusinessKey(), event.getStatus());
    }
}
