package com.miyu.cloud.dms.api.maintainapplication.listener;

import cn.iocoder.yudao.module.bpm.event.BpmProcessInstanceStatusEvent;
import cn.iocoder.yudao.module.bpm.event.BpmProcessInstanceStatusEventListener;
import com.miyu.cloud.dms.api.maintainapplication.MaintainApplicationApi;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class MaintainApplicationStatusListener extends BpmProcessInstanceStatusEventListener {
    @Resource
    private MaintainApplicationApi maintainApplicationApi;

    @Override
    protected String getProcessDefinitionKey() {
        return MaintainApplicationApi.PROCESS_KEY;
    }

    @Override
    protected void onEvent(BpmProcessInstanceStatusEvent event) {
        maintainApplicationApi.updateMaintenanceStatus(event.getBusinessKey(), event.getStatus());
    }
}
