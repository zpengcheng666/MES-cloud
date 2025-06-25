package com.miyu.module.dc.controller.admin.offlinealarm.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class OfflineAlarmResVO {

    @Schema(description = "时间")
    private String time;

    @Schema(description = "topicId", example = "1")
    private String topicId;

    @Schema(description = "deviceId", example = "1")
    private String deviceId;

    private String productTypeName;

    private Integer commType;

    private String alarmData;

    private Integer alarmType;
}
