package com.miyu.module.qms.core.job;

import cn.iocoder.yudao.framework.tenant.core.job.TenantJob;
import com.miyu.module.qms.service.inspectiontool.InspectionToolService;
import com.xxl.job.core.handler.annotation.XxlJob;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

@Component
@Transactional
public class QmsJob {

    @Resource
    private InspectionToolService inspectionToolService;

    // 定时处理
    @XxlJob("handleInspectionToolJob")
    @TenantJob
    public void handleInspectionToolJob() throws Exception {
        System.out.println("开始处理检测工具检定时间"+Thread.currentThread().getName());
        inspectionToolService.createInspectionToolVerificationRecord();
    }
}
