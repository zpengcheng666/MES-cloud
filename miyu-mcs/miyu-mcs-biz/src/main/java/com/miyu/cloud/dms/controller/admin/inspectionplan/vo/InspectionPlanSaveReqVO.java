package com.miyu.cloud.dms.controller.admin.inspectionplan.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

@Schema(description = "管理后台 - 设备检查计划新增/修改 Request VO")
@Data
public class InspectionPlanSaveReqVO {

    @Schema(description = "id", requiredMode = Schema.RequiredMode.REQUIRED, example = "10995")
    private String id;

    @Schema(description = "计划编码")
    private String code;

    @Schema(description = "所属计划关联树")
    private String tree;

    @Schema(description = "设备")
    private String device;

    @Schema(description = "启用状态", example = "2")
    private Integer enableStatus;

    @Schema(description = "是否超期停机", example = "2")
    private Integer expirationShutdown;

    @Schema(description = "超期时间")
    private Integer expirationTime;

    @Schema(description = "检查类型", example = "1")
    private Integer type;

    @Schema(description = "负责人")
    private String superintendent;

    @Schema(description = "开始时间")
    private LocalDateTime startTime;

    @Schema(description = "cron表达式")
    private String cornExpression;

    @Schema(description = "计划任务id")
    private String jobId;

    @Schema(description = "检查内容")
    private String content;

    @Schema(description = "说明", example = "你说的对")
    private String remark;

}
