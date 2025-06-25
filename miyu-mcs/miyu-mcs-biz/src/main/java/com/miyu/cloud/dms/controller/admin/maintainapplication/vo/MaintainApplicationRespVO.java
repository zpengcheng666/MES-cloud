package com.miyu.cloud.dms.controller.admin.maintainapplication.vo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

@Schema(description = "管理后台 - 设备维修申请 Response VO")
@Data
@ExcelIgnoreUnannotated
public class MaintainApplicationRespVO {

    @Schema(description = "id", requiredMode = Schema.RequiredMode.REQUIRED, example = "6665")
    @ExcelProperty("id")
    private String id;

    @Schema(description = "设备id")
    @ExcelProperty("设备id")
    private String device;

    @Schema(description = "设备编码")
    @ExcelProperty("设备编码")
    private String code;

    @Schema(description = "设备名称")
    @ExcelProperty("设备名称")
    private String name;

    @Schema(description = "生产单元编号")
    @ExcelProperty("生产单元编号")
    private String processingUnitNumber;

    @Schema(description = "设备型号")
    @ExcelProperty("设备型号")
    private String model;

    @Schema(description = "关键设备")
    @ExcelProperty("关键设备")
    private Integer important;

    @Schema(description = "维修类型")
    @ExcelProperty("维修类型")
    private Integer type;

    @Schema(description = "故障信息描述")
    @ExcelProperty("故障信息描述")
    private String describe1;

    @Schema(description = "期望修复时间")
    @ExcelProperty("期望修复时间")
    private Integer duration;

    @Schema(description = "申请状态")
    @ExcelProperty("申请状态")
    private Integer status;

    @Schema(description = "流程实例编号")
    @ExcelProperty("流程实例编号")
    private String processInstanceId;

    @Schema(description = "申请人")
    @ExcelProperty("申请人")
    private String applicant;

    @Schema(description = "申请时间")
    @ExcelProperty("申请时间")
    private LocalDateTime applicationTime;

    @Schema(description = "创建时间")
    @ExcelProperty("创建时间")
    private LocalDateTime createTime;

}
