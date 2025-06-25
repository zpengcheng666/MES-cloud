package com.miyu.module.ppm.listener;

import cn.iocoder.yudao.module.bpm.event.BpmProcessInstanceStatusEvent;
import cn.iocoder.yudao.module.bpm.event.BpmProcessInstanceStatusEventListener;
import com.miyu.module.ppm.api.shippinginstorage.ShippingInstorageApi;
import com.miyu.module.ppm.enums.ApiConstants;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 委托加工收货审核监听
 *
 * @author zhp
 */
@Component
public class DMInstorageAuditStatusListener extends BpmProcessInstanceStatusEventListener {

    @Resource
    private ShippingInstorageApi shippingInstorageApi;

    @Override
    public String getProcessDefinitionKey() {
        return ApiConstants.SHIPPING_INSTORAGE_PROCESS_KEY;
    }

    @Override
    protected void onEvent(BpmProcessInstanceStatusEvent event) {
        shippingInstorageApi.updateShippingInstorageStatus(event.getBusinessKey(), event.getStatus());
    }

}
