package com.miyu.module.pdm.controller.admin.processTask.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.mzt.logapi.starter.annotation.DiffLogField;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.List;

@Schema(description = "管理后台 - 工艺任务项目列表 Request VO")
@Data
@ToString(callSuper = true)
public class ProcessTaskReqVO {
    @Schema(description = "工艺任务id", example = "20041")
    private String id;

    @Schema(description = "项目编号")
    private String projectCode;

    @Schema(description = "零件号")
    private String partVersionId;

    @Schema(description = "加工状态")
    private String processCondition;

    @Schema(description = "零件图号", example = "A220")
    private String partNumber;

    @Schema(description = "评估状态")
    private String status;

    @Schema(description = "负责人")
    private String reviewedBy;

    @Schema(description = "负责人Id")
    private String reviewer;

    @Schema(description = "任务Id", example = "20041")
    private String taskId;

    @Schema(description = "评估截止时间", requiredMode = Schema.RequiredMode.REQUIRED, example = "描述。。。。")
    @DiffLogField(name="截止时间")
    private LocalDateTime deadline;

    @Schema(description = "流程实例的编号", example = "20041")
    private String processInstanceId;

    @Schema(description = "流程审批状态", example = "1")
    private Integer approvalStatus;

    @TableField
    @Schema(description = "零部件版本id数组", example = "1")
    List<String> partVersionIdArr;

    @TableField
    @Schema(description = "任务号数组", example = "1")
    List<String> taskIdArr;
}
