package com.miyu.module.ppm.listener;

import cn.iocoder.yudao.module.bpm.event.BpmProcessInstanceStatusEvent;
import cn.iocoder.yudao.module.bpm.event.BpmProcessInstanceStatusEventListener;
import com.miyu.module.ppm.api.contractconsignment.ContractConsignmentApi;
import com.miyu.module.ppm.api.contractconsignment.ContractConsignmentReturnApi;
import com.miyu.module.ppm.enums.ApiConstants;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 外协退货流程审批的结果的监听器实现类
 *
 * @author zhp
 */
@Component
public class ContractConsignmentReturnStatusListener extends BpmProcessInstanceStatusEventListener {

    @Resource
    private ContractConsignmentReturnApi contractConsignmentReturnApi;

    /**
     * 监听采购收货审批
     * @return
     */
    @Override
    protected String getProcessDefinitionKey() {
        return ApiConstants.CONTRACT_CONSIGNMENT_RETURN_PROCESS_KEY;
    }

    @Override
    protected void onEvent(BpmProcessInstanceStatusEvent event) {
        contractConsignmentReturnApi.updateContractConsignmentReturnStatus(event.getBusinessKey(), event.getStatus());
    }
}
