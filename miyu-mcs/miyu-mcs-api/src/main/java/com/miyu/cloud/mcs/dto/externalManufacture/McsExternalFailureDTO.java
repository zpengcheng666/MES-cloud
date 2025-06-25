package com.miyu.cloud.mcs.dto.externalManufacture;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class McsExternalFailureDTO {

    //设备
    @Schema(description = "设备编码", requiredMode = Schema.RequiredMode.REQUIRED, example = "abc")
    private String deviceCode;
    //异常编码
    @Schema(description = "异常编码", requiredMode = Schema.RequiredMode.REQUIRED, example = "AB-404")
    private String code;
    //故障状态
    @Schema(description = "故障状态(0:未处理;1:处理中;2:已解决)", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    private Integer faultState;
    //故障时间
    @Schema(description = "故障时间")
    private LocalDateTime faultTime;
    //修复时间
    @Schema(description = "修复时间")
    private LocalDateTime repairTime;

    //故障描述
    @Schema(description = "故障描述")
    private String description;
    //故障原因
    @Schema(description = "故障原因")
    private String cause;
    //维修人员
    @Schema(description = "维修人员")
    private String maintenanceBy;
    //修复费用
    @Schema(description = "修复费用")
    private Double restorationCosts;
    //备注
    @Schema(description = "备注")
    private String remarks;
    //故障类型码 todo
    @Schema(description = "故障类型码")
    private String typeCode;
}
