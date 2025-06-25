package com.miyu.module.wms.controller.admin.alarm.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import javax.validation.constraints.*;

@Schema(description = "管理后台 - 异常新增/修改 Request VO")
@Data
public class AlarmSaveReqVO {

    @Schema(description = "ID", requiredMode = Schema.RequiredMode.REQUIRED)
    private String id;

    @Schema(description = "告警类型(1-系统,2-agv)")
    private String alarmType;

    @Schema(description = "告警源")
    private String alarmSource;

    @Schema(description = "告警级别(1-信息,2-警告,3-错误)")
    private String alarmLevel;

    @Schema(description = "告警编号")
    private String alarmNum;

    @Schema(description = "告警描述")
    private String alarmDesc;

    @Schema(description = "异常状态,1-未解决,2-已解决,3-忽略")
    private String alarmState;

}