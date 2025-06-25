package com.miyu.module.pdm.controller.admin.processDetailTask.vo;


import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

@Schema(description = "管理后台 - 工艺任务详细 Response VO")
@Data
@ExcelIgnoreUnannotated
public class ProcessDetailTaskRespVO {

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

    @Schema(description = "任务Id", example = "20041")
    private String taskId;
}
