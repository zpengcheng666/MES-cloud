package com.miyu.module.ppm.core.job;

import cn.iocoder.yudao.framework.tenant.core.job.TenantJob;
import com.miyu.module.ppm.service.consignmentreturn.ConsignmentReturnService;
import com.miyu.module.ppm.service.contractconsignment.ContractConsignmentService;
import com.xxl.job.core.handler.annotation.XxlJob;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

@Component
@Transactional
public class ConsignmentOutboundJob {


    @Resource
    private ContractConsignmentService contractConsignmentService;
    // 查询外协出库的结果
    @XxlJob("handlePPMConsignmentOutBoundResultJob")
//    @TenantIgnore
    @TenantJob
    public void handlePPMConsignmentOutBoundResultJob() throws Exception {
        System.out.println("外协出库查看出库结果"+Thread.currentThread().getName());
        contractConsignmentService.checkOutBoundInfo();
    }

}
