package com.miyu.cloud.dms.controller.admin.calendardevice.provo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

@Schema(description = "管理后台 - 设备日历关联部分,记录了设备绑定了那些班次 Response VO")
@Data
@ExcelIgnoreUnannotated
public class CalendarProductionlineRespVO {

    @Schema(description = "id", requiredMode = Schema.RequiredMode.REQUIRED, example = "15996")
    @ExcelProperty("id")
    private String id;

    @Schema(description = "设备id", example = "25921")
    @ExcelProperty("设备id")
    private String deviceId;

    @Schema(description = "设备编号")
    @ExcelProperty("设备编号")
    private String deviceCode;

    @Schema(description = "设备名称", example = "芋艿")
    @ExcelProperty("设备名称")
    private String deviceName;

    @Schema(description = "班次id", example = "32660")
    @ExcelProperty("班次id")
    private String shiftId;

    @Schema(description = "班次名称", example = "芋艿")
    @ExcelProperty("班次名称")
    private String shiftName;

    @Schema(description = "创建时间")
    @ExcelProperty("创建时间")
    private LocalDateTime createTime;

}
