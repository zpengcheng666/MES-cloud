package com.miyu.module.pdm.controller.admin.toolingTask.vo;

import com.alibaba.excel.annotation.ExcelProperty;
import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.ToString;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Schema(description = "管理后台 - 工装任务项目列表 Request VO")
@Data
@ToString(callSuper = true)
public class ToolingTaskReqVO {

    @Schema(description = "id", requiredMode = Schema.RequiredMode.REQUIRED, example = "1382")
    @ExcelProperty("id")
    private String id;

    @Schema(description = "tid", requiredMode = Schema.RequiredMode.REQUIRED, example = "1382")
    @ExcelProperty("tid")
    private String tid;

    @Schema(description = "申请单号(GS+年月日+三位流水，如GS20240101001)", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("申请单号(GS+年月日+三位流水，如GS20240101001)")
    private String applyCode;

    @Schema(description = "工装图号", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("工装图号")
    private String toolingNumber;

    @Schema(description = "工装编号(唯一，即类码)", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("工装编号(唯一，即类码)")
    private String toolingCode;

    @Schema(description = "工装名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "张三")
    @ExcelProperty("工装名称")
    private String toolingName;

    @Schema(description = "工装分类编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "12163")
    @ExcelProperty("工装分类编号")
    private Long categoryId;

    @Schema(description = "申请人(默认当前登录人-可更改)", requiredMode = Schema.RequiredMode.REQUIRED, example = "赵六")
    @ExcelProperty("申请人(默认当前登录人-可更改)")
    private String applyName;

    @Schema(description = "文件名称", example = "2")
    @ExcelProperty("文件名称")
    private String fileName;

    @Schema(description = "文件类型", example = "2")
    @ExcelProperty("文件类型")
    private String fileType;

    @Schema(description = "客户化标识", requiredMode = Schema.RequiredMode.REQUIRED, example = "张三")
    @ExcelProperty("客户化标识")
    private String customizedIndex;

    @Schema(description = "申请部门", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("申请部门")
    private String applyDepartment;

    @Schema(description = "工装分类名", example = "1",requiredMode = Schema.RequiredMode.REQUIRED)
    private String name;

    @Schema(description = "数量", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("数量")
    private Integer quantity;

    @Schema(description = "申请理由：1新品试制2生产需要3工艺更新4原工装损坏或报废5其它", requiredMode = Schema.RequiredMode.REQUIRED, example = "不香")
    @ExcelProperty("申请理由：1新品试制2生产需要3工艺更新4原工装损坏或报废5其它")
    private String applyReason;

    @Schema(description = "需求日期", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("需求日期")
    private LocalDateTime requireTime;

    @Schema(description = "设备名称", example = "王五")
    @ExcelProperty("设备名称")
    private String equipmentName;

    @Schema(description = "设备型号")
    @ExcelProperty("设备型号")
    private String equipmentNumber;

    @Schema(description = "工装形式(1通用 0专用)", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("工装形式(1通用 0专用)")
    private String toolingStyle;

    @Schema(description = "加工类型(0采购 1外委 2自制 3改造)", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @ExcelProperty("加工类型(0采购 1外委 2自制 3改造)")
    private String processType;

    @Schema(description = "技术条件")
    @ExcelProperty("技术条件")
    private String technicalRequirement;

    @Schema(description = "状态(0工作中 1审批中 2审批失败 3已完成)", example = "2")
    @ExcelProperty("状态(0工作中 1审批中 2审批失败 3已完成)")
    private String status;

    @Schema(description = "流程实例的编号", example = "25257")
    @ExcelProperty("流程实例的编号")
    private String processInstanceId;

    @Schema(description = "流程审批状态", example = "2")
    @ExcelProperty("流程审批状态")
    private Integer approvalStatus;


    @Schema(description = "创建时间")
    @ExcelProperty("创建时间")
    private LocalDateTime createTime;

    @TableField(exist = false)
    @Schema(description = "工装申请id")
    @ExcelProperty("工装申请id")
    private String toolingApplyId;

    @TableField(exist = false)
    @Schema(description = "负责人")
    private String reviewedBy;

    @TableField(exist = false)
    @Schema(description = "负责人Id")
    private String reviewer;

    @TableField(exist = false)
    @Schema(description = "设计截止日期")
    private LocalDateTime deadline;

    @Schema(description = "工装申请id数组", example = "1")
    List<String> toolingApplyIdArr;
}
