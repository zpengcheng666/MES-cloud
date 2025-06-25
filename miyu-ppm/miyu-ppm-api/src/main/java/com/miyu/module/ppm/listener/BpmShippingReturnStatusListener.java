package com.miyu.module.ppm.listener;


import cn.iocoder.yudao.module.bpm.event.BpmProcessInstanceStatusEvent;
import cn.iocoder.yudao.module.bpm.event.BpmProcessInstanceStatusEventListener;
import com.miyu.module.ppm.api.shippingreturn.ShippingReturnApi;
import com.miyu.module.ppm.enums.ApiConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 销售退货审批的监听器实现类
 *
 * @author 芋道源码
 */
@Component
@Slf4j
public class BpmShippingReturnStatusListener extends BpmProcessInstanceStatusEventListener {

    @Resource
    private ShippingReturnApi shippingReturnApi;

    @Override
    protected String getProcessDefinitionKey() {
        return ApiConstants.RETURN_PROCESS_KEY;
    }

    @Override
    protected void onEvent(BpmProcessInstanceStatusEvent event) {

        log.info("回调成功");
        //更新审批状态
        shippingReturnApi.updateShippingReturnStatus(event.getBusinessKey(), event.getStatus());
    }

}
