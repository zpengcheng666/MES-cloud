package com.miyu.cloud.mcs.core.job;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.tenant.core.job.TenantJob;
import com.miyu.cloud.mcs.restServer.api.ManufacturingService;
import com.xxl.job.core.handler.annotation.XxlJob;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@Component
@Transactional
public class McsScheduledJob {

    @Resource
    private ManufacturingService manufacturingService;

    //搬运结果监听
    @XxlJob("handleMCSCarryResultJob")
//    @TenantIgnore
    @TenantJob
    public void handleMCSCarryResultJob() throws Exception {
        manufacturingService.deliveryCheck();
    }

    //零件检查结果监听
    @XxlJob("handleMCSInspectionResultJob")
//    @TenantIgnore
    @TenantJob
    public CommonResult<?> handleMCSInspectionResultJob() throws Exception {
        return manufacturingService.inspectionCheck();
    }

    //零件自动搬运
    @XxlJob("handleMCSCarryTaskJob")
//    @TenantIgnore
    @TenantJob
    public CommonResult<?> handleMCSCarryTaskJob() throws Exception {
        return manufacturingService.carryTaskCheck();
    }

    @XxlJob("handleMCSCarryStatisticsJob")
    @TenantJob
    public void handleMCSCarryStatisticsJob() throws Exception {
        manufacturingService.setCarryStatisticsCache();
        manufacturingService.setMaterialCarryReadyStatusCache();
    }

    @XxlJob("handleMCSCleanCarryTaskJob")
    @TenantJob
    public void handleMCSCleanCarryTaskJob() throws Exception {
        manufacturingService.cleanAbandonedCarryTask();
    }
}
