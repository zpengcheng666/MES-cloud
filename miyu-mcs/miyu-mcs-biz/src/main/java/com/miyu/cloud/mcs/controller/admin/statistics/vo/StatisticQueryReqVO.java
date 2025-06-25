package com.miyu.cloud.mcs.controller.admin.statistics.vo;

import lombok.Data;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Data
public class StatisticQueryReqVO {

    private static final Integer PAGE_NO = 1;
    private static final Integer PAGE_SIZE = 10;
    private static final Integer INTERVAL_DEF = 1;
    private static final ChronoUnit UNIT_DEF = ChronoUnit.MONTHS;
    private static final LocalDateTime END_TIME = LocalDateTime.now();

    private String workerId;
    private Integer pageNo;
    private Integer pageSize;
    private Integer interval;
    private ChronoUnit unit;
    private LocalDateTime beginTime;
    private LocalDateTime endTime;

    public Integer getPageNo() {
        return pageNo == null ? PAGE_NO : pageNo;
    }

    public Integer getPageSize() {
        return pageSize == null ? PAGE_SIZE : pageSize;
    }

    public LocalDateTime getBeginTime() {
        if (beginTime != null) return beginTime;
        else {
            LocalDateTime time = getEndTime();
            return time.minus(interval == null ? INTERVAL_DEF : interval, unit == null ? UNIT_DEF : unit);
        }
    }

    public LocalDateTime getEndTime() {
        return endTime == null ? END_TIME : endTime;
    }

}
