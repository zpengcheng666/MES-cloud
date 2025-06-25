package com.miyu.module.pdm.controller.admin.toolingTask.vo;

import com.alibaba.excel.annotation.ExcelProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;



import javax.validation.constraints.*;
import java.time.LocalDateTime;

@Schema(description = "管理后台 - 工装申请信息新增/修改 Request VO")
@Data
public class ToolingTaskSaveReqVO {

    @Schema(description = "id", requiredMode = Schema.RequiredMode.REQUIRED, example = "1382")
    private String id;

    @Schema(description = "申请单号(GS+年月日+三位流水，如GS20240101001)", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotEmpty(message = "申请单号(GS+年月日+三位流水，如GS20240101001)不能为空")
    private String applyCode;

    @Schema(description = "工装图号", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("工装图号")
    private String toolingNumber;

    @Schema(description = "工装编号(唯一，即类码)", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotEmpty(message = "工装编号(唯一，即类码)不能为空")
    private String toolingCode;

    @Schema(description = "工装名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "张三")
    @NotEmpty(message = "工装名称不能为空")
    private String toolingName;

    @Schema(description = "工装分类编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "12163")
    @NotNull(message = "工装分类编号不能为空")
    private Long categoryId;

    @Schema(description = "申请人(默认当前登录人-可更改)", requiredMode = Schema.RequiredMode.REQUIRED, example = "赵六")
    @NotEmpty(message = "申请人(默认当前登录人-可更改)不能为空")
    private String applyName;

    @Schema(description = "申请部门", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotEmpty(message = "申请部门不能为空")
    private String applyDepartment;

    @Schema(description = "数量", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "数量不能为空")
    private Integer quantity;

    @Schema(description = "申请理由：1新品试制2生产需要3工艺更新4原工装损坏或报废5其它", requiredMode = Schema.RequiredMode.REQUIRED, example = "不香")
    @NotEmpty(message = "申请理由：1新品试制2生产需要3工艺更新4原工装损坏或报废5其它不能为空")
    private String applyReason;

    @Schema(description = "需求日期", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "需求日期不能为空")
    private LocalDateTime requireTime;

    @Schema(description = "设备名称", example = "王五")
    private String equipmentName;

    @Schema(description = "设备型号")
    private String equipmentNumber;
    @Schema(description = "设计截止日期")
    private LocalDateTime deadline;

    @Schema(description = "工装形式(1通用 0专用)", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotEmpty(message = "工装形式(1通用 0专用)不能为空")
    private String toolingStyle;

    @Schema(description = "工装类别(0制造工装 1装配工装 2试验工装 3检验工装)", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @NotEmpty(message = "工装类别(0制造工装 1装配工装 2试验工装 3检验工装)不能为空")
    private String toolingType;

    @Schema(description = "加工类型(0采购 1外委 2自制 3改造)", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @NotEmpty(message = "加工类型(0采购 1外委 2自制 3改造)不能为空")
    private String processType;

    @Schema(description = "技术条件")
    private String technicalRequirement;

    @Schema(description = "状态(0工作中 1审批中 2审批失败 3已完成)", example = "2")
    private Integer status;

    @Schema(description = "流程实例的编号", example = "25257")
    private String processInstanceId;

    @Schema(description = "流程审批状态", example = "2")
    private Integer approvalStatus;

}