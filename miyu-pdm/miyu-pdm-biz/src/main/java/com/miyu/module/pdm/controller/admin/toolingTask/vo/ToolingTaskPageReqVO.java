package com.miyu.module.pdm.controller.admin.toolingTask.vo;

import lombok.*;
import io.swagger.v3.oas.annotations.media.Schema;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static cn.iocoder.yudao.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

@Schema(description = "管理后台 - 工装申请信息分页 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class ToolingTaskPageReqVO extends PageParam {

    @Schema(description = "申请单号(GS+年月日+三位流水，如GS20240101001)")
    private String applyCode;

    @Schema(description = "产品图号")
    private String productNumber;

    @Schema(description = "工装编号(唯一，即类码)")
    private String toolingCode;

    @Schema(description = "工装名称", example = "张三")
    private String toolingName;

    @Schema(description = "工装分类编号", example = "12163")
    private Long categoryId;

    @Schema(description = "申请人(默认当前登录人-可更改)", example = "赵六")
    private String applyName;

    @Schema(description = "申请部门")
    private String applyDepartment;

    @Schema(description = "数量")
    private Integer quantity;

    @Schema(description = "申请理由：1新品试制2生产需要3工艺更新4原工装损坏或报废5其它", example = "不香")
    private String applyReason;

    @Schema(description = "需求日期")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDate[] requireTime;

    @Schema(description = "设备名称", example = "王五")
    private String equipmentName;

    @Schema(description = "设备型号")
    private String equipmentNumber;

    @Schema(description = "工装形式(1通用 0专用)")
    private String toolingStyle;

    @Schema(description = "工装类别(0制造工装 1装配工装 2试验工装 3检验工装)", example = "1")
    private String toolingType;

    @Schema(description = "加工类型(0采购 1外委 2自制 3改造)", example = "1")
    private String processType;

    @Schema(description = "技术条件")
    private String technicalRequirement;

    @Schema(description = "状态(0工作中 1审批中 2审批失败 3已完成)", example = "2")
    private Integer status;

    @Schema(description = "流程实例的编号", example = "25257")
    private String processInstanceId;

    @Schema(description = "流程审批状态", example = "2")
    private Integer approvalStatus;

    @Schema(description = "创建时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] createTime;

}