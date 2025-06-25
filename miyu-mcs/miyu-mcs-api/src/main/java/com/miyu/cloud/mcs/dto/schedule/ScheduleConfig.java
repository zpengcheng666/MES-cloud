package com.miyu.cloud.mcs.dto.schedule;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Data
public class ScheduleConfig {

    //起始时间
    private LocalDateTime startTime;
    //资源最大使用数量
    private List<ScheduleResourceType> maxResourcesCount;
    //参与排产设备
    private List<ScheduleLedger> ledgerList;
    //参与排产任务
    private List<SchedulePlan> planList;
}
