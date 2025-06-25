package com.miyu.cloud.mcs.controller.admin.batchorderapsresult.vo;

import com.miyu.cloud.mcs.dto.schedule.ScheduleResourceType;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class OrderScheduleStrategy {

    private boolean customerTime;
    private boolean resourceLimitations;
    private List<ScheduleResourceType> resource;
    private LocalDateTime time;

    public List<ScheduleResourceType> getResource() {
        if (!resourceLimitations) return null;
        return resource;
    }

    public LocalDateTime getTime() {
        if (!customerTime) return LocalDateTime.now();
        return time;
    }
}
