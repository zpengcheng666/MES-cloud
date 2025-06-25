package com.miyu.module.ppm.api.contract.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Schema(description = "合同订单")
public class ContractOrderDTO {
    @Schema(description = "合同订单id", requiredMode = Schema.RequiredMode.REQUIRED, example = "20529")
    private String id;

    @Schema(description = "合同id", requiredMode = Schema.RequiredMode.REQUIRED, example = "20529")
    private String contractId;

    @Schema(description = "产品名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "20529")
    private String materialId;

    @Schema(description = "产品数量", requiredMode = Schema.RequiredMode.REQUIRED, example = "123.000")
    private BigDecimal quantity;

    @Schema(description = "产品单价", requiredMode = Schema.RequiredMode.REQUIRED, example = "123.00")
    private BigDecimal price;

    @Schema(description = "税率", requiredMode = Schema.RequiredMode.REQUIRED, example = "123.00")
    private BigDecimal taxRate;

    @Schema(description = "含税单价", requiredMode = Schema.RequiredMode.REQUIRED, example = "123.00")
    private BigDecimal taxPrice;

    @Schema(description = "交货周期", requiredMode = Schema.RequiredMode.REQUIRED)
    private LocalDateTime leadDate;

    @Schema(description = "产品单位", requiredMode = Schema.RequiredMode.REQUIRED, example = "李四")
    private String materialUnit;

    private String productId;

    private String productName;

    private String avgPrice;

    private String maxPrice;

    private String minPrice;

    private String latestPrice;

    private String initTax;


    private String projectId;
    /***
     * 订单ID
     */
    private String orderId;
    /***
     * 项目子计划ID
     */
    private String projectPlanId;
}
