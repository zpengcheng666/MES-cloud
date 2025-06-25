package com.miyu.module.ppm.core.job;

import cn.iocoder.yudao.framework.tenant.core.job.TenantJob;
import com.miyu.module.ppm.service.shipping.ShippingService;
import com.miyu.module.wms.enums.DictConstants;
import com.xxl.job.core.handler.annotation.XxlJob;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

@Component
@Transactional
public class ShippingJob {


    @Resource
    private ShippingService shippingService;
    // 定时处理 发货单出库结果
    @XxlJob("handleDMOutBoundResultJob")
//    @TenantIgnore
    @TenantJob
    public void handleDMOutBoundResultJob() throws Exception {
        System.out.println("销售发货单查看出库结果"+Thread.currentThread().getName());
        shippingService.checkOutBoundInfo();
    }

}
