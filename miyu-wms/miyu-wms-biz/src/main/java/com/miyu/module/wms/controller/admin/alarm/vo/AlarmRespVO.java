package com.miyu.module.wms.controller.admin.alarm.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import java.util.*;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;
import com.alibaba.excel.annotation.*;
import cn.iocoder.yudao.framework.excel.core.annotations.DictFormat;
import cn.iocoder.yudao.framework.excel.core.convert.DictConvert;

@Schema(description = "管理后台 - 异常 Response VO")
@Data
@ExcelIgnoreUnannotated
public class AlarmRespVO {

    @Schema(description = "ID", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("ID")
    private String id;

    @Schema(description = "创建时间", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("创建时间")
    private LocalDateTime createTime;

    @Schema(description = "告警类型(1-系统,2-agv)")
    @ExcelProperty(value = "告警类型(1-系统,2-agv)", converter = DictConvert.class)
    @DictFormat("wms_alarm_type") // TODO 代码优化：建议设置到对应的 DictTypeConstants 枚举类中
    private String alarmType;

    @Schema(description = "告警源")
    @ExcelProperty("告警源")
    private String alarmSource;

    @Schema(description = "告警级别(1-信息,2-警告,3-错误)")
    @ExcelProperty(value = "告警级别(1-信息,2-警告,3-错误)", converter = DictConvert.class)
    @DictFormat("wms_alarm_level") // TODO 代码优化：建议设置到对应的 DictTypeConstants 枚举类中
    private String alarmLevel;

    @Schema(description = "告警编号")
    @ExcelProperty("告警编号")
    private String alarmNum;

    @Schema(description = "告警描述")
    @ExcelProperty("告警描述")
    private String alarmDesc;

    @Schema(description = "告警解除时间")
    @ExcelProperty("告警解除时间")
    private LocalDateTime alarmTime;

    @Schema(description = "异常状态,1-未解决,2-已解决,3-忽略")
    @ExcelProperty(value = "异常状态,1-未解决,2-已解决,3-忽略", converter = DictConvert.class)
    @DictFormat("wms_alarm_state") // TODO 代码优化：建议设置到对应的 DictTypeConstants 枚举类中
    private String alarmState;

}