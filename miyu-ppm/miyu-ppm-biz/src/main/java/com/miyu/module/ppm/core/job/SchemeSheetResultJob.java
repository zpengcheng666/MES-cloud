package com.miyu.module.ppm.core.job;

import cn.iocoder.yudao.framework.tenant.core.job.TenantJob;
import com.miyu.module.ppm.service.consignmentreturn.ConsignmentReturnService;
import com.miyu.module.ppm.service.purchaseconsignment.PurchaseConsignmentService;
import com.xxl.job.core.handler.annotation.XxlJob;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

@Component
@Transactional
public class SchemeSheetResultJob {


    @Resource
    private PurchaseConsignmentService purchaseConsignmentService;
    // 定时处理 采购入库检验单结果
    @XxlJob("handlePPMSchemeSheetResultJob")
//    @TenantIgnore
    @TenantJob
    public void handlePPMSchemeSheetResultJob() throws Exception {
        System.out.println("采购入库调用质检"+Thread.currentThread().getName());
        purchaseConsignmentService.checkSchemeSheet();
    }

}
