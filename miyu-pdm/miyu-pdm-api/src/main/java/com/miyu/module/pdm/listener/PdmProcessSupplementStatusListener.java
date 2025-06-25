package com.miyu.module.pdm.listener;

import cn.iocoder.yudao.module.bpm.event.BpmProcessInstanceStatusEvent;
import cn.iocoder.yudao.module.bpm.event.BpmProcessInstanceStatusEventListener;
import com.miyu.module.pdm.api.processSupplement.ProcessSupplementApi;
import com.miyu.module.pdm.enums.ApiConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
@Slf4j
public class PdmProcessSupplementStatusListener extends BpmProcessInstanceStatusEventListener {
    @Resource
    private ProcessSupplementApi processSupplementApi;

    @Override
    protected String getProcessDefinitionKey() {
        return ApiConstants.PROCESS_SUPPLEMENT_PROCESS_KEY;
    }

    @Override
    protected void onEvent(BpmProcessInstanceStatusEvent event) {
        log.info("审批回调成功");
        //更新审批状态
        processSupplementApi.updateProcessSupplementInstanceStatus(event.getBusinessKey(), event.getStatus());
    }

}
