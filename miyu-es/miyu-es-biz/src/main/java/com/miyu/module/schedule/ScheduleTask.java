package com.miyu.module.schedule;

import com.miyu.module.es.service.brake.BrakeService;
import com.miyu.module.es.service.brake.BrakeServiceImpl;
import com.xxl.job.core.handler.annotation.XxlJob;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

@Component
@Transactional
public class ScheduleTask {

    @Resource
    BrakeServiceImpl brakeServiceImpl;

    @XxlJob("syncBrakeJob")
    public void syncBrakeJob(){
        System.out.println("==========================定时任务 车闸自动同步 开始执行!==================================");
        brakeServiceImpl.syncAllBrake();
        System.out.println("==========================定时任务 车闸自动同步 执行完毕!==================================");
    }
}
