package com.miyu.cloud.mcs.listener;

import cn.iocoder.yudao.module.bpm.event.BpmProcessInstanceStatusEvent;
import cn.iocoder.yudao.module.bpm.event.BpmProcessInstanceStatusEventListener;
import com.miyu.cloud.mcs.api.McsDistributionApplicationApi;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class McsApplicationStatusListener extends BpmProcessInstanceStatusEventListener {

    @Resource
    private McsDistributionApplicationApi distributionApplicationApi;

    @Override
    protected String getProcessDefinitionKey() {
        return McsDistributionApplicationApi.PROCESS_KEY;
    }

    @Override
    protected void onEvent(BpmProcessInstanceStatusEvent event) {
        distributionApplicationApi.updateApplicationStatus(event.getBusinessKey(), event.getStatus());
    }

}
