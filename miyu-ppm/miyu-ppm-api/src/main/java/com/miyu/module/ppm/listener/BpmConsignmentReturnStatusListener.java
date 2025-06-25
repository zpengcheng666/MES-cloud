package com.miyu.module.ppm.listener;

import cn.iocoder.yudao.module.bpm.event.BpmProcessInstanceStatusEvent;
import cn.iocoder.yudao.module.bpm.event.BpmProcessInstanceStatusEventListener;
import com.miyu.module.ppm.api.consignmentReturn.ConsignmentReturnApi;
import com.miyu.module.ppm.enums.ApiConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/***
 * 采购退货审批流程调用
 */
@Component
@Slf4j
public class BpmConsignmentReturnStatusListener extends BpmProcessInstanceStatusEventListener {

    @Resource
    private ConsignmentReturnApi consignmentReturnApi;

    @Override
    protected String getProcessDefinitionKey() {
        return ApiConstants.PM_RETURN_AUDIT_KEY;
    }

    @Override
    protected void onEvent(BpmProcessInstanceStatusEvent event) {

        log.info("回调成功");
        //更新审批状态
        consignmentReturnApi.updateConsignmentReturnStatus(event.getBusinessKey(), event.getStatus());

    }
}
