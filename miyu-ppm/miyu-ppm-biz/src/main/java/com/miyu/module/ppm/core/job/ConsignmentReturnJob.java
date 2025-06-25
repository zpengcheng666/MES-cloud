package com.miyu.module.ppm.core.job;

import cn.iocoder.yudao.framework.tenant.core.job.TenantJob;
import com.miyu.module.ppm.service.consignmentreturn.ConsignmentReturnService;
import com.xxl.job.core.handler.annotation.XxlJob;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

@Component
@Transactional
public class ConsignmentReturnJob {


    @Resource
    private ConsignmentReturnService consignmentReturnService;
    // 定时处理 采购退货单出库结果
    @XxlJob("handlePPMOutBoundResultJob")
//    @TenantIgnore
    @TenantJob
    public void handlePPMOutBoundResultJob() throws Exception {
        System.out.println("采购退货单查看出库结果"+Thread.currentThread().getName());
        consignmentReturnService.checkOutBoundInfo();
    }

}
