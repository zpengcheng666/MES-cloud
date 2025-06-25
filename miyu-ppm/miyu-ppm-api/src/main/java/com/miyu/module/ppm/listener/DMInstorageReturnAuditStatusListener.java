package com.miyu.module.ppm.listener;

import cn.iocoder.yudao.module.bpm.event.BpmProcessInstanceStatusEvent;
import cn.iocoder.yudao.module.bpm.event.BpmProcessInstanceStatusEventListener;
import com.miyu.module.ppm.api.shippinginstorage.ShippingInstorageApi;
import com.miyu.module.ppm.api.shippinginstorage.ShippingInstorageReturnApi;
import com.miyu.module.ppm.enums.ApiConstants;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 委托加工退货
 *
 * @author zhp
 */
@Component
public class DMInstorageReturnAuditStatusListener extends BpmProcessInstanceStatusEventListener {

    @Resource
    private ShippingInstorageReturnApi shippingInstorageReturnApi;

    @Override
    public String getProcessDefinitionKey() {
        return ApiConstants.SHIPPING_INSTORAGE_RETURN_PROCESS_KEY;
    }

    @Override
    protected void onEvent(BpmProcessInstanceStatusEvent event) {
        shippingInstorageReturnApi.updateShippingInstorageReturnStatus(event.getBusinessKey(), event.getStatus());
    }

}
