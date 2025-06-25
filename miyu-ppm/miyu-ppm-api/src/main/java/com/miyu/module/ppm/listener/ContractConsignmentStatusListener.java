package com.miyu.module.ppm.listener;

import cn.iocoder.yudao.module.bpm.event.BpmProcessInstanceStatusEvent;
import cn.iocoder.yudao.module.bpm.event.BpmProcessInstanceStatusEventListener;
import com.miyu.module.ppm.api.contractconsignment.ContractConsignmentApi;
import com.miyu.module.ppm.api.purchaseConsignment.PurchaseConsignmentApi;
import com.miyu.module.ppm.enums.ApiConstants;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 外协发货流程审批的结果的监听器实现类
 *
 * @author zhp
 */
@Component
public class ContractConsignmentStatusListener extends BpmProcessInstanceStatusEventListener {

    @Resource
    private ContractConsignmentApi contractConsignmentApi;

    /**
     * 监听采购收货审批
     * @return
     */
    @Override
    protected String getProcessDefinitionKey() {
        return ApiConstants.CONTRACT_CONSIGNMENT_PROCESS_KEY;
    }

    @Override
    protected void onEvent(BpmProcessInstanceStatusEvent event) {
        contractConsignmentApi.updateContractConsignmentStatus(event.getBusinessKey(), event.getStatus());
    }
}
