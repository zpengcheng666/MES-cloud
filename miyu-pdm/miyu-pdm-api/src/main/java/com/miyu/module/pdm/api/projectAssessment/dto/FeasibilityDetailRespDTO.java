package com.miyu.module.pdm.api.projectAssessment.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema(description = "RPC 服务 - 设备刀具工装")
@Data
public class FeasibilityDetailRespDTO {

    private String id;

    private String projectCode;

    private String partVersionId;

    private Integer resourcesType;

    private String resourcesTypeId;

    private Integer quantity;

    private String processingTime;

    private String partNumber;

    /** 采购类型，0:已有 1:需采购*/
    private Integer purchaseType;
}
