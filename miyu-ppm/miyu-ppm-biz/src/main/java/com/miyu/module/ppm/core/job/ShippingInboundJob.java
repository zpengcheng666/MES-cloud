package com.miyu.module.ppm.core.job;

import cn.iocoder.yudao.framework.tenant.core.job.TenantJob;
import com.miyu.module.ppm.service.shipping.ShippingService;
import com.miyu.module.ppm.service.shippinginstorage.ShippingInstorageService;
import com.xxl.job.core.handler.annotation.XxlJob;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

@Component
@Transactional
public class ShippingInboundJob {


    @Resource
    private ShippingInstorageService shippingInstorageService;
    // 定时处理 销售订单入库单查看入库结果
    @XxlJob("handleDMInBoundResultJob")
//    @TenantIgnore
    @TenantJob
    public void handleDMInBoundResultJob() throws Exception {
        System.out.println("销售订单入库单查看入库结果"+Thread.currentThread().getName());
        shippingInstorageService.checkInBoundInfo();
    }

}
