package com.miyu.cloud.mcs.dto.manufacture;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class McsBatchResourceDTO {

    @Schema(description = "物料id", requiredMode = Schema.RequiredMode.REQUIRED)
    private String materialUid;

    @Schema(description = "物料类型id", requiredMode = Schema.RequiredMode.REQUIRED)
    private String materialConfigId;

    @Schema(description = "物料编号", requiredMode = Schema.RequiredMode.REQUIRED)
    private String materialNumber;

    @Schema(description = "物料条码", requiredMode = Schema.RequiredMode.REQUIRED)
    private String barCode;

    @Schema(description = "物料批次号", requiredMode = Schema.RequiredMode.REQUIRED)
    private String batchNumber;

    @Schema(description = "资源类型", requiredMode = Schema.RequiredMode.REQUIRED)
    private String resourceType;

    @Schema(description = "物料管理模式，1：单件，2：批量", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer materialManage;

    @Schema(description = "总库存")
    private Integer totality;
}
