package com.miyu.module.wms.controller.admin.alarm.vo;

import lombok.*;
import java.util.*;
import io.swagger.v3.oas.annotations.media.Schema;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;

import static cn.iocoder.yudao.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

@Schema(description = "管理后台 - 异常分页 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class AlarmPageReqVO extends PageParam {

    @Schema(description = "创建时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] createTime;

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

    @Schema(description = "告警解除时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] alarmTime;

    @Schema(description = "异常状态,1-未解决,2-已解决,3-忽略")
    private String alarmState;

}