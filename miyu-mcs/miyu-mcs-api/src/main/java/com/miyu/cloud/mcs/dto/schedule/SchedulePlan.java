package com.miyu.cloud.mcs.dto.schedule;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
public class SchedulePlan {

    //id
    private String id;
    //子任务编码
    private String number;
    //项目号
    private String projectNumber;
    //零件图号
    private String partNumber;
    //工艺id
    private String technologyId;
    //优先级
    private Integer priority;
    //数量
    private Integer count;
    //接收时间
    private LocalDateTime receptionTime;
    //交付时间
    private LocalDateTime deliveryTime;
    //是否为首件
    private Boolean isFirst;
    //工序列表
    private List<ScheduleProcess> processList;

    private List<ScheduleJob> taskJobs = new ArrayList<>();
}
