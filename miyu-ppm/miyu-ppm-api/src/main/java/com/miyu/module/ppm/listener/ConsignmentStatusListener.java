package com.miyu.module.ppm.listener;

import cn.iocoder.yudao.module.bpm.event.BpmProcessInstanceStatusEvent;
import cn.iocoder.yudao.module.bpm.event.BpmProcessInstanceStatusEventListener;
import com.miyu.module.ppm.api.purchaseConsignment.PurchaseConsignmentApi;
import com.miyu.module.ppm.enums.ApiConstants;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 采购流程审批的结果的监听器实现类
 *
 * @author Zhangyunfei
 */
@Component
public class ConsignmentStatusListener extends BpmProcessInstanceStatusEventListener {

    @Resource
    private PurchaseConsignmentApi purchaseConsignmentApi;

    /**
     * 监听采购收货审批
     * @return
     */
    @Override
    protected String getProcessDefinitionKey() {
        return ApiConstants.CONSIGNMENT_PROCESS_KEY;
    }

    @Override
    protected void onEvent(BpmProcessInstanceStatusEvent event) {
        purchaseConsignmentApi.updatePurchaseConsignmentStatus(event.getBusinessKey(), event.getStatus());
    }
}
