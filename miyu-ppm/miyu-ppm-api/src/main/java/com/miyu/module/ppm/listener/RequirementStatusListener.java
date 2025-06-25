package com.miyu.module.ppm.listener;

import cn.iocoder.yudao.module.bpm.event.BpmProcessInstanceStatusEvent;
import cn.iocoder.yudao.module.bpm.event.BpmProcessInstanceStatusEventListener;
import com.miyu.module.ppm.api.purchaseRequirement.RequirementApi;
import com.miyu.module.ppm.enums.ApiConstants;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 采购申请审批的结果的监听器实现类
 *
 * @author Zhangyunfei
 */
@Component
public class RequirementStatusListener extends BpmProcessInstanceStatusEventListener {

    @Resource
    private RequirementApi requirementApi;

    @Override
    public String getProcessDefinitionKey() {
        return ApiConstants.PURCHASE_REQUIREMENT_PROCESS_KEY;
    }

    @Override
    protected void onEvent(BpmProcessInstanceStatusEvent event) {
        requirementApi.updateRequirementAuditStatus(event.getBusinessKey(), event.getStatus());
    }

}
