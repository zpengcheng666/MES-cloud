package com.miyu.cloud.dms.controller.admin.maintenancerecord.vo;

import cn.iocoder.yudao.framework.excel.core.annotations.DictFormat;
import cn.iocoder.yudao.framework.excel.core.convert.DictConvert;
import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

@Schema(description = "管理后台 - 设备保养维护记录 Response VO")
@Data
@ExcelIgnoreUnannotated
public class MaintenanceRecordRespVO {

    @Schema(description = "id", requiredMode = Schema.RequiredMode.REQUIRED, example = "22653")
    @ExcelProperty("id")
    private String id;

    @Schema(description = "计划编码")
    @ExcelProperty("计划编码")
    private String code;

    @Schema(description = "保养维护记录状态")
    @ExcelProperty(value = "保养维护记录状态", converter = DictConvert.class)
    @DictFormat("dms_maintenance_record_status")
    private Integer recordStatus;

    @Schema(description = "设备")
    @ExcelProperty("设备")
    private String device;

    @Schema(description = "是否为关键设备")
    @ExcelProperty("是否为关键设备")
    private Integer criticalDevice;

    @Schema(description = "是否超期停机", example = "2")
    @ExcelProperty(value = "是否超期停机", converter = DictConvert.class)
    @DictFormat("dms_expiration_shutdown")
    private Integer expirationShutdown;

    @Schema(description = "超期时间")
    @ExcelProperty("超期时间")
    private Integer expirationTime;

    @Schema(description = "保养类型", example = "2")
    @ExcelProperty(value = "保养类型", converter = DictConvert.class)
    @DictFormat("DMS_MAINTENANCE_TYPE")
    private Integer type;

    @Schema(description = "完成状态", example = "2")
    @ExcelProperty(value = "完成状态", converter = DictConvert.class)
    @DictFormat("DMS_MAINTENANCE_STATUS")
    private Integer status;

    @Schema(description = "备注")
    @ExcelProperty("备注")
    private String remarks;

    @Schema(description = "保养内容")
    @ExcelProperty("保养内容")
    private String content;

    @Schema(description = "保养人")
    @ExcelProperty("保养人")
    private String maintenanceBy;

    @Schema(description = "开始维护时间")
    @ExcelProperty("开始维护时间")
    private LocalDateTime startTime;

    @Schema(description = "结束维护时间")
    @ExcelProperty("结束维护时间")
    private LocalDateTime endTime;

    @Schema(description = "创建时间")
    @ExcelProperty("创建时间")
    private LocalDateTime createTime;

}
