package com.miyu.module.ppm.controller.admin.purchaserequirement.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDateTime;
import java.util.*;
import javax.validation.constraints.*;
import java.math.BigDecimal;

@Schema(description = "管理后台 - 采购需求明细，可以来源于采购申请或MRP新增/修改 Request VO")
@Data
public class PurchaseRequirementDetailSaveReqVO {

    @Schema(description = "主键", requiredMode = Schema.RequiredMode.REQUIRED, example = "11930")
    private String id;

    @Schema(description = "申请单ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "17326")
    @NotEmpty(message = "申请单ID不能为空")
    private String requirementId;

    @Schema(description = "源单类型", example = "1")
    private Integer sourceType;

    @Schema(description = "源单id", example = "22119")
    private String sourceId;

    @Schema(description = "需求物料", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotEmpty(message = "需求物料不能为空")
    private String requiredMaterial;

    @Schema(description = "需求数量", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "需求数量不能为空")
    private BigDecimal requiredQuantity;

    @Schema(description = "需求时间")
    private LocalDateTime requiredDate;

    @Schema(description = "预算单价", example = "6721")
    private BigDecimal estimatedPrice;

    @Schema(description = "供应商，即企业ID")
    private String supplier;

    @Schema(description = "创建IP")
    private String creationIp;

    @Schema(description = "更新ip")
    private String updatedIp;

}