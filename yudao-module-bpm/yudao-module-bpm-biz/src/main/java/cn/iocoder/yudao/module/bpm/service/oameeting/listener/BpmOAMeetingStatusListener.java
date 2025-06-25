package cn.iocoder.yudao.module.bpm.service.oameeting.listener;

import cn.iocoder.yudao.module.bpm.event.BpmProcessInstanceStatusEvent;
import cn.iocoder.yudao.module.bpm.event.BpmProcessInstanceStatusEventListener;
import cn.iocoder.yudao.module.bpm.service.oameeting.OaMeetingService;
import cn.iocoder.yudao.module.bpm.service.oameeting.OaMeetingServiceImpl;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * OA 请假单的结果的监听器实现类
 *
 * @author 芋道源码
 */
@Component
public class BpmOAMeetingStatusListener extends BpmProcessInstanceStatusEventListener {

    @Resource
    private OaMeetingService oaMeetingService;

    @Override
    protected String getProcessDefinitionKey() {
        return OaMeetingServiceImpl.PROCESS_KEY;
    }

    @Override
    protected void onEvent(BpmProcessInstanceStatusEvent event) {
        oaMeetingService.updateLeaveStatus(Long.parseLong(event.getBusinessKey()), event.getStatus());
    }

}
