package com.miyu.module.pdm.controller.admin.toolingApply.vo;

import cn.iocoder.yudao.framework.common.pojo.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Date;

@Schema(description = "PDM - 工装申请 Request VO")
@Data
public class ToolingApplyReqVO extends PageParam {

    @Schema(description = "id", example = "1")
    private Integer pageNo;

    @Schema(description = "id", example = "1")
    private Integer pageSize;

    @Schema(description = "id", example = "1")
    private String id;

    @Schema(description = "工装分类编号", example = "2142sda")
    private Long categoryId;

    @Schema(description = "工装编号(唯一，即类码)", example = "芋道")
    private String toolingCode;

    @Schema(description = "工装图号", example = "芋道")
    private String toolingNumber;

    @Schema(description = "工装名称", example = "芋道")
    private String toolingName;

    @Schema(description = "版次", example = "芋道")
    private String toolingVersion;

    @Schema(description = "申请人(默认当前登录人-可更改)", example = "芋道")
    private String applyName;

    @Schema(description = "数量", example = "芋道")
    private Integer quantity;

    @Schema(description = "工装形式(1通用 0专用)", example = "芋道")
    private String toolingStyle;

    @Schema(description = "加工类型(0采购 1外委 2自制 3改造)", example = "芋道")
    private String processType;

    @Schema(description = "申请理由：1新品试制2生产需要3工艺更新4原工装损坏或报废5其它", example = "芋道")
    private String applyReason;

    @Schema(description = "需求日期", example = "芋道")
    private LocalDateTime requireTime;

    @Schema(description = "设备名称", example = "芋道")
    private String equipmentName;

    @Schema(description = "设备型号", example = "芋道")
    private String equipmentNumber;

    @Schema(description = "技术条件", example = "芋道")
    private String technicalRequirement;

    @Schema(description = "客户化标识", example = "芋道")
    private String customizedIndex;

    @Schema(description = "状态(0工作中 1审批中 2审批失败 3已完成)", example = "0")
    private Integer status;

    @Schema(description = "流程实例的编号", example = "213")
    private String processInstanceId;

    @Schema(description = "流程审批状态", example = "1")
    private Integer approvalStatus;

    @Schema(description = "工装分类名", example = "1")
    private String name;

    @Schema(description = "申请人ID", example = "1")
    private String applyId;

    @Schema(description = "分类表", example = "213")
    private String parentId;
}
