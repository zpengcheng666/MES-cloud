package com.miyu.cloud.macs.listener;

import cn.iocoder.yudao.module.bpm.event.BpmProcessInstanceStatusEvent;
import cn.iocoder.yudao.module.bpm.event.BpmProcessInstanceStatusEventListener;
import com.miyu.cloud.macs.api.AccessApplicationControlApi;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * OA 请假单的结果的监听器实现类
 *
 * @author 芋道源码
 */
@Component
public class MacsApplicationStatusListener extends BpmProcessInstanceStatusEventListener {

    @Resource
    private AccessApplicationControlApi accessApplicationApi;

    @Override
    protected String getProcessDefinitionKey() {
        return AccessApplicationControlApi.PROCESS_KEY;
    }

    @Override
    protected void onEvent(BpmProcessInstanceStatusEvent event) {
        accessApplicationApi.updateApplicationStatus(event.getBusinessKey(), event.getStatus());
    }

}
