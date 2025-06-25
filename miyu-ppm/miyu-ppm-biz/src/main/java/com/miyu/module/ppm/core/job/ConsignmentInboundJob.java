package com.miyu.module.ppm.core.job;

import cn.iocoder.yudao.framework.tenant.core.job.TenantJob;
import com.miyu.module.ppm.service.contractconsignment.ContractConsignmentService;
import com.miyu.module.ppm.service.purchaseconsignment.PurchaseConsignmentService;
import com.xxl.job.core.handler.annotation.XxlJob;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

@Component
@Transactional
public class ConsignmentInboundJob {


    @Resource
    private PurchaseConsignmentService purchaseConsignmentService;
    // 查询采购入库的结果
    @XxlJob("handlePPMConsignmentInBoundResultJob")
//    @TenantIgnore
    @TenantJob
    public void handlePPMConsignmentInBoundResultJob() throws Exception {
        System.out.println("采购入库查看入库结果"+Thread.currentThread().getName());
        //purchaseConsignmentService.checkInBoundInfo();
        purchaseConsignmentService.checkInBoundInfo1();
    }

}
