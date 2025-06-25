package com.miyu.cloud.dms.controller.admin.calendarshift.vo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

@Schema(description = "管理后台 - 班次类型 Response VO")
@Data
@ExcelIgnoreUnannotated
public class ShiftTypeRespVO {

    @Schema(description = "id", requiredMode = Schema.RequiredMode.REQUIRED, example = "25449")
    @ExcelProperty("id")
    private String id;

    @Schema(description = "班次名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "张三")
    @ExcelProperty("班次名称")
    private String name;

    @Schema(description = "基础日历id", requiredMode = Schema.RequiredMode.REQUIRED, example = "25449")
    @ExcelProperty("基础日历id")
    private String bcid;

    @Schema(description = "基础日历名", requiredMode = Schema.RequiredMode.REQUIRED, example = "张三")
    @ExcelProperty("基础日历名")
    private String basicName;

    @Schema(description = "创建时间")
    @ExcelProperty("创建时间")
    private LocalDateTime createTime;

    @Schema(description = "开始日期", requiredMode = Schema.RequiredMode.REQUIRED, example = "张三")
    private String startDate;

    @Schema(description = "结束日期", requiredMode = Schema.RequiredMode.REQUIRED, example = "张三")
    private String endDate;

    @Schema(description = "设备id", requiredMode = Schema.RequiredMode.REQUIRED, example = "张三")
    private String deviceId;

}
