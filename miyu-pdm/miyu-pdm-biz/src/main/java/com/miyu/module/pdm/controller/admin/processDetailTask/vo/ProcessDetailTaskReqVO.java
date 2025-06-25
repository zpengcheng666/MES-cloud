package com.miyu.module.pdm.controller.admin.processDetailTask.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.List;


@Schema(description = "管理后台 - 项目编号列表 Request VO")
@Data
@ToString(callSuper = true)
public class ProcessDetailTaskReqVO {
    @Schema(description = "任务id", example = "20041")
    private String id;

    @Schema(description = "项目编号")
    private String projectCode;

    @Schema(description = "零部件版本ID")
    private String partVersionId;

    @Schema(description = "工艺版本ID")
    private String processVersionId;

    @Schema(description = "工序ID")
    private String procedureId;

    @TableField
    @Schema(description = "工序ID")
    List<String> procedureIdArr;

    @TableField
    @Schema(description = "零部件版本id数组", example = "1")
    List<String> partVersionIdArr;

    @TableField
    @Schema(description = "工艺规程版本id数组", example = "1")
    List<String> processVersionIdArr;

    @Schema(description = "负责人ID")
    private String reviewedBy;

    @Schema(description = "负责人姓名")
    private String reviewer;

    @Schema(description = "评估截止日期", requiredMode = Schema.RequiredMode.REQUIRED, example = "1024")
    private LocalDateTime deadline;

    @Schema(description = "状态")
    private Integer status;

    @Schema(description = "流程实例的编号-工艺详细设计")
    private String processInstanceId;

    @Schema(description = "流程审批状态-工艺详细设计")
    private Integer approvalStatus;

    @Schema(description = "零件图号")
    private String partNumber;


}
