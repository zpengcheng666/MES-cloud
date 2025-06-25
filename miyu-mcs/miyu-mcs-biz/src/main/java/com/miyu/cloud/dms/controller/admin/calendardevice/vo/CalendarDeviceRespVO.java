package com.miyu.cloud.dms.controller.admin.calendardevice.vo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Schema(description = "管理后台 - 设备日历,记录设备每天的可用时间 Response VO")
@Data
@ExcelIgnoreUnannotated
public class CalendarDeviceRespVO {

    @Schema(description = "id", requiredMode = Schema.RequiredMode.REQUIRED, example = "23908")
    @ExcelProperty("id")
    private String id;

    @Schema(description = "设备id", requiredMode = Schema.RequiredMode.REQUIRED, example = "2716")
    @ExcelProperty("设备id")
    private String deviceId;

    @Schema(description = "日期")
    @ExcelProperty("日期")
    private LocalDate date;

    @Schema(description = "开始时间")
    @ExcelProperty("开始时间")
    private LocalTime startTime;

    @Schema(description = "结束时间")
    @ExcelProperty("结束时间")
    private LocalTime endTime;

    @Schema(description = "创建时间")
    @ExcelProperty("创建时间")
    private LocalDateTime createTime;

    private String dateStr;
    private String startStr;
    private String endStr;

}
