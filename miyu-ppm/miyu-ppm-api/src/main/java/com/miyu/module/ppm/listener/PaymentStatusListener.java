package com.miyu.module.ppm.listener;

import cn.iocoder.yudao.module.bpm.event.BpmProcessInstanceStatusEvent;
import cn.iocoder.yudao.module.bpm.event.BpmProcessInstanceStatusEventListener;
import com.miyu.module.ppm.api.contract.PaymentApi;
import com.miyu.module.ppm.enums.ApiConstants;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 合同审批的结果的监听器实现类
 *
 * @author Zhangyunfei
 */
@Component
public class PaymentStatusListener extends BpmProcessInstanceStatusEventListener {

    @Resource
    private PaymentApi paymentApi;

    @Override
    public String getProcessDefinitionKey() {
        return ApiConstants.CONTRACT_PAYMENT_AUDIT_PROCESS_KEY;
    }

    @Override
    protected void onEvent(BpmProcessInstanceStatusEvent event) {
        paymentApi.updatePaymentAuditStatus(event.getBusinessKey(), event.getStatus());
    }

}
