package com.miyu.module.pdm.dal.dataobject.toolingTask;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import com.baomidou.mybatisplus.annotation.*;
import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 工装申请信息 DO
 */
@TableName("pdm_tooling_task")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ToolingTaskDO extends BaseDO {

    /**
     * id
     */
    @TableId(type = IdType.ASSIGN_ID)
    private String id;
    /**
     * 申请单号(GS+年月日+三位流水，如GS20240101001)
     */

    @TableField(exist = false)
    private String tid;

    @TableField(exist = false)
    private String applyCode;
    /**
     * 工装编号(唯一，即类码)
     */
    @TableField(exist = false)
    private String toolingCode;
    /**
     * 工装名称
     */
    @TableField(exist = false)
    private String toolingName;
    /**
     * 工装分类编号
     */
    @TableField(exist = false)
    private Long categoryId;
    /**
     * 申请人(默认当前登录人-可更改)
     */
    @TableField(exist = false)
    private String applyName;
    /**
     * 申请部门
     */
    @TableField(exist = false)
    private String applyDepartment;
    /**
     * 数量
     */
    @TableField(exist = false)
    private Integer quantity;
    /**
     * 申请理由：1新品试制2生产需要3工艺更新4原工装损坏或报废5其它
     */
    @TableField(exist = false)
    private String applyReason;
    /**
     * 需求日期
     */
    @TableField(exist = false)
    private LocalDateTime requireTime;
    /**
     * 设备名称
     */
    @TableField(exist = false)
    private String equipmentName;
    /**
     * 设备型号
     */
    @TableField(exist = false)
    private String equipmentNumber;
    /**
     * 工装形式(1通用 0专用)
     */
    @TableField(exist = false)
    private String toolingStyle;

    /**
     * 加工类型(0采购 1外委 2自制 3改造)
     */
    @TableField(exist = false)
    private String processType;
    /**
     * 技术条件
     */
    @TableField(exist = false)
    private String technicalRequirement;
    /**
     * 状态(0工作中 1审批中 2审批失败 3已完成)
     */
    private Integer status;
    /**
     * 流程实例的编号
     */

    private String processInstanceId;
    /**
     * 流程审批状态
     */
    private Integer approvalStatus;

    /**
     * 工装申请id
     */
    private String toolingApplyId;

    @Schema(description = "负责人")
    private String reviewedBy;

    @Schema(description = "设计员(用户账号)")
    private String username;

    @Schema(description = "设计截止日期")
    private LocalDateTime deadline;


    @Schema(description = "负责人Id")
    private String reviewer;

    @TableField(exist = false)
    private String name;
}