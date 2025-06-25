package com.miyu.module.ppm.listener;


import cn.iocoder.yudao.module.bpm.event.BpmProcessInstanceStatusEvent;
import cn.iocoder.yudao.module.bpm.event.BpmProcessInstanceStatusEventListener;
import com.miyu.module.ppm.api.contracrefund.ContractRefundApi;
import com.miyu.module.ppm.enums.ApiConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 合同退款审批的监听器实现类
 *
 * @author 芋道源码
 */
@Component
@Slf4j
public class BpmContractRefundStatusListener extends BpmProcessInstanceStatusEventListener {

    @Resource
    private ContractRefundApi contractRefundApi;

    @Override
    protected String getProcessDefinitionKey() {
        return ApiConstants.REFUND_PROCESS_KEY;
    }

    @Override
    protected void onEvent(BpmProcessInstanceStatusEvent event) {

        log.info("回调成功");
        //更新审批状态
        contractRefundApi.updateContractRefundStatus(event.getBusinessKey(), event.getStatus());
    }

}
