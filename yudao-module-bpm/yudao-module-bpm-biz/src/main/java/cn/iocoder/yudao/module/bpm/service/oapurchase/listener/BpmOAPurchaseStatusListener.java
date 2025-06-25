package cn.iocoder.yudao.module.bpm.service.oapurchase.listener;

import cn.iocoder.yudao.module.bpm.event.BpmProcessInstanceStatusEvent;
import cn.iocoder.yudao.module.bpm.event.BpmProcessInstanceStatusEventListener;
import cn.iocoder.yudao.module.bpm.service.oapurchase.OaPurchaseService;
import cn.iocoder.yudao.module.bpm.service.oapurchase.OaPurchaseServiceImpl;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * OA 请假单的结果的监听器实现类
 *
 * @author 芋道源码
 */
@Component
public class BpmOAPurchaseStatusListener extends BpmProcessInstanceStatusEventListener {

    @Resource
    private OaPurchaseService oaPurchaseService;

    @Override
    protected String getProcessDefinitionKey() {
        return OaPurchaseServiceImpl.PROCESS_KEY;
    }

    @Override
    protected void onEvent(BpmProcessInstanceStatusEvent event) {
        oaPurchaseService.updateLeaveStatus(Long.parseLong(event.getBusinessKey()), event.getStatus());
    }

}
