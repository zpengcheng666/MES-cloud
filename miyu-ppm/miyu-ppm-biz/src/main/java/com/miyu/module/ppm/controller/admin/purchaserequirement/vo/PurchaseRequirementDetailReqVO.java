package com.miyu.module.ppm.controller.admin.purchaserequirement.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.ToString;

@Schema(description = "管理后台 - 采购需求明细 Request VO")
@Data
@ToString(callSuper = true)
public class PurchaseRequirementDetailReqVO {

    @Schema(description = "申请单ID", example = "17326")
    private String requirementId;

    @Schema(description = "是否有效", example = "1")
    private Integer isValid;
}