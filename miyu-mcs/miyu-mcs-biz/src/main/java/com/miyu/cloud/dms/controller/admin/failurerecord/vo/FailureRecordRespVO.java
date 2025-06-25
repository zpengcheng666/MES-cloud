package com.miyu.cloud.dms.controller.admin.failurerecord.vo;

import cn.iocoder.yudao.framework.excel.core.annotations.DictFormat;
import cn.iocoder.yudao.framework.excel.core.convert.DictConvert;
import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

@Schema(description = "管理后台 - 异常记录 Response VO")
@Data
@ExcelIgnoreUnannotated
public class FailureRecordRespVO {

    @Schema(description = "id", requiredMode = Schema.RequiredMode.REQUIRED, example = "27168")
    @ExcelProperty("id")
    private String id;

    @Schema(description = "设备")
    @ExcelProperty("设备")
    private String device;

    @Schema(description = "异常编码")
    @ExcelProperty("异常编码")
    private String code;

    @Schema(description = "故障状态")
    @ExcelProperty(value = "故障状态", converter = DictConvert.class)
    @DictFormat("dms_fault_state")
    private String faultState;

    @Schema(description = "故障描述", example = "你猜")
    @ExcelProperty("故障描述")
    private String description;

    @Schema(description = "故障原因")
    @ExcelProperty("故障原因")
    private String cause;

    @Schema(description = "故障时间")
    @ExcelProperty("故障时间")
    private LocalDateTime faultTime;

    @Schema(description = "维修人员")
    @ExcelProperty("维修人员")
    private String maintenanceBy;

    @Schema(description = "修复时间")
    @ExcelProperty("修复时间")
    private LocalDateTime repairTime;

    @Schema(description = "修复费用")
    @ExcelProperty("修复费用")
    private Double restorationCosts;

    @Schema(description = "备注")
    @ExcelProperty("备注")
    private String remarks;

    @Schema(description = "创建时间")
    @ExcelProperty("创建时间")
    private LocalDateTime createTime;
}
