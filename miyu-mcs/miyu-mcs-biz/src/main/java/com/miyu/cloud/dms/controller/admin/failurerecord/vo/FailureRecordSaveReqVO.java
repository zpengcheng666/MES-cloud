package com.miyu.cloud.dms.controller.admin.failurerecord.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

@Schema(description = "管理后台 - 异常记录新增/修改 Request VO")
@Data
public class FailureRecordSaveReqVO {

    @Schema(description = "id", requiredMode = Schema.RequiredMode.REQUIRED, example = "27168")
    private String id;

    @Schema(description = "设备")
    private String device;

    @Schema(description = "异常编码")
    private String code;

    @Schema(description = "故障状态")
    private String faultState;

    @Schema(description = "故障描述", example = "你猜")
    private String description;

    @Schema(description = "故障原因")
    private String cause;

    @Schema(description = "故障时间")
    private LocalDateTime faultTime;

    @Schema(description = "维修人员")
    private String maintenanceBy;

    @Schema(description = "修复时间")
    private LocalDateTime repairTime;

    @Schema(description = "修复费用")
    private Double restorationCosts;

    @Schema(description = "备注")
    private String remarks;

    @Schema(description = "创建时间")
    private LocalDateTime createTime;
}
