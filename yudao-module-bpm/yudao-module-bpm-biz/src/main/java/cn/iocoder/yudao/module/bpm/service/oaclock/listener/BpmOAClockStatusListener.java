package cn.iocoder.yudao.module.bpm.service.oaclock.listener;

import cn.iocoder.yudao.module.bpm.event.BpmProcessInstanceStatusEvent;
import cn.iocoder.yudao.module.bpm.event.BpmProcessInstanceStatusEventListener;
import cn.iocoder.yudao.module.bpm.service.oaclock.OaClockService;
import cn.iocoder.yudao.module.bpm.service.oaclock.OaClockServiceImpl;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * OA 请假单的结果的监听器实现类
 *
 * @author 芋道源码
 */
@Component
public class BpmOAClockStatusListener extends BpmProcessInstanceStatusEventListener {

    @Resource
    private OaClockService oaClockService;

    @Override
    protected String getProcessDefinitionKey() {
        return OaClockServiceImpl.PROCESS_KEY;
    }

    @Override
    protected void onEvent(BpmProcessInstanceStatusEvent event) {
        oaClockService.updateLeaveStatus(Long.parseLong(event.getBusinessKey()), event.getStatus());
    }

}
