package com.miyu.cloud.dms.controller.admin.maintenanceplan.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

@Schema(description = "管理后台 - 设备保养维护计划新增/修改 Request VO")
@Data
public class MaintenancePlanSaveReqVO {

    @Schema(description = "id", requiredMode = Schema.RequiredMode.REQUIRED, example = "14340")
    private String id;

    @Schema(description = "计划编码")
    private String code;

    @Schema(description = "所属计划关联树")
    private String tree;

    @Schema(description = "设备")
    private String device;

    @Schema(description = "是否为关键设备")
    private Integer criticalDevice;

    @Schema(description = "启用状态", example = "1")
    private Integer enableStatus;

    @Schema(description = "是否超期停机", example = "2")
    private Integer expirationShutdown;

    @Schema(description = "超期时间")
    private Integer expirationTime;

    @Schema(description = "维护类型", example = "1")
    private Integer type;

    @Schema(description = "开始时间")
    private LocalDateTime startTime;

    @Schema(description = "cron表达式")
    private String cornExpression;

    @Schema(description = "计划任务id")
    private String jobId;

    @Schema(description = "维护内容")
    private String content;

    @Schema(description = "说明", example = "你猜")
    private String remark;

    @Schema(description = "负责人")
    private String superintendent;

    @Schema(description = "最后一次保养时间")
    private LocalDateTime lastTime;

    @Schema(description = "上一次完成状态", example = "1")
    private Integer lastStatus;

}
