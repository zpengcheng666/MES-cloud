package com.miyu.cloud.mcs.dto.schedule;

import lombok.Data;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Data
public class ScheduleJob {
    //批次任务
    private String batchId;
    private int index;
    private List<ScheduleJobProcess> taskSteps = new ArrayList<>();
    public ScheduleJob(String batchId,int index) {
        this.batchId = batchId;
        this.index = index;
    }

    public ScheduleJob() {
    }

    //预计开始时间
    private Date planStartTime;
    //预计结束时间
    private Date planEndTime;
}
