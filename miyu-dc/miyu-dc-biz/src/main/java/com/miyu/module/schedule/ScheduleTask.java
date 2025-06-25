package com.miyu.module.schedule;

import cn.iocoder.yudao.framework.tenant.core.job.TenantJob;
import com.miyu.module.dc.service.producttype.ProductTypeServiceImpl;
import com.xxl.job.core.handler.annotation.XxlJob;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

@Component
@Transactional
public class ScheduleTask {

    @Resource
    ProductTypeServiceImpl productTypeService;

    /**
     *校验当前系统设备在线
     */
    @XxlJob("getOnlineStatusJob")
//    @TenantJob
    public void getOnlineStatusJob(){
        System.out.println("==========================定时任务-设备在线校验 开始执行!==================================");
        productTypeService.getOnlineStatus();
        System.out.println("==========================定时任务-设备在线校验 执行完毕!==================================");
    }

}
