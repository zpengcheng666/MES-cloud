package cn.iocoder.yudao.module.bpm.listen.pms;

import cn.iocoder.yudao.module.bpm.api.pms.PmsListenerApi;
import cn.iocoder.yudao.module.bpm.event.BpmProcessInstanceStatusEvent;
import cn.iocoder.yudao.module.bpm.event.BpmProcessInstanceStatusEventListener;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 评审监听器实现类
 *
 * @author 芋道源码
 */
@Component
public class PmsPlanStatusListener extends BpmProcessInstanceStatusEventListener {

    @Resource
    private PmsListenerApi pmsListenerApi;

    @Override
    protected String getProcessDefinitionKey() {
        return PmsKeyCollect.PLAN_KEY;
    }

    @Override
    protected void onEvent(BpmProcessInstanceStatusEvent event) {
        pmsListenerApi.updatePlanStatus(event.getBusinessKey(),event.getStatus());
    }

}
