package cn.iocoder.yudao.module.bpm.service.oasupply.listener;

import cn.iocoder.yudao.module.bpm.event.BpmProcessInstanceStatusEvent;
import cn.iocoder.yudao.module.bpm.event.BpmProcessInstanceStatusEventListener;
import cn.iocoder.yudao.module.bpm.service.oasupply.OaSupplyService;
import cn.iocoder.yudao.module.bpm.service.oasupply.OaSupplyServiceImpl;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * OA 请假单的结果的监听器实现类
 *
 * @author 芋道源码
 */
@Component
public class BpmOASupplyStatusListener extends BpmProcessInstanceStatusEventListener {

    @Resource
    private OaSupplyService oaSupplyService;

    @Override
    protected String getProcessDefinitionKey() {
        return OaSupplyServiceImpl.PROCESS_KEY;
    }

    @Override
    protected void onEvent(BpmProcessInstanceStatusEvent event) {
        oaSupplyService.updateLeaveStatus(Long.parseLong(event.getBusinessKey()), event.getStatus());
    }

}
