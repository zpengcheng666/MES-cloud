package com.miyu.cloud.mcs.dto.manufacture;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class McsStepRecordDTO {

    //id
    private String id;
    //工序任务id
    private String batchRecordId;
    //工步序号
    private String stepNumber;
    //工步id
    private String stepId;
    //工步名称
    private String stepName;
    //指定设备
    private String deviceId;
    //指定设备
    private String deviceNumber;
    //计划开始时间
    private LocalDateTime planStartTime;
    //计划结束时间
    private LocalDateTime planEndTime;
    //状态
    private Integer status;
    //计划开始时间
    private LocalDateTime startTime;
    //计划结束时间
    private LocalDateTime endTime;
}
