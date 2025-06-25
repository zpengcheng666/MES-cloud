package com.miyu.cloud.dms.controller.admin.calendar.vo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

@Schema(description = "管理后台 - 基础日历 Response VO")
@Data
@ExcelIgnoreUnannotated
public class BasicCalendarRespVO {

    @Schema(description = "id", requiredMode = Schema.RequiredMode.REQUIRED, example = "20306")
    @ExcelProperty("id")
    private String id;

    @Schema(description = "日历名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "王五")
    @ExcelProperty("日历名称")
    private String name;

    @Schema(description = "开始日期", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("开始日期")
    private LocalDateTime startDate;

    @Schema(description = "结束日期", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("结束日期")
    private LocalDateTime endDate;

}
