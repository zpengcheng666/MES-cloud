package com.miyu.cloud.dms.controller.admin.calendarshift.vo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;
import java.time.LocalTime;

@Schema(description = "管理后台 - 班次时间")
@Data
@ExcelIgnoreUnannotated
public class ShiftTimeRespVO {
    @Schema(description = "id", requiredMode = Schema.RequiredMode.REQUIRED, example = "23908")
    @ExcelProperty("id")
    private String id;

    @Schema(description = "typeId", requiredMode = Schema.RequiredMode.REQUIRED, example = "23908")
    @ExcelProperty("typeId")
    private String typeId;

    @Schema(description = "开始时间")
    @ExcelProperty("开始时间")
    private LocalTime startTime;

    @Schema(description = "结束时间")
    @ExcelProperty("结束时间")
    private LocalTime endTime;

    private String startStr;
    private String endStr;
}
