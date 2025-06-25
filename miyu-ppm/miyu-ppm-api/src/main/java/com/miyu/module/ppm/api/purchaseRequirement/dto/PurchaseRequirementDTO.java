package com.miyu.module.ppm.api.purchaseRequirement.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Schema(description = "管理后台 - 采购申请DTO")
@Data
public class PurchaseRequirementDTO {

    @Schema(description = "主键", requiredMode = Schema.RequiredMode.REQUIRED, example = "81")
    private String id;

    @Schema(description = "采购类型", example = "2")
    private Integer type;

    @Schema(description = "申请人")
    private String applicant;

    @Schema(description = "申请部门")
    private String applicationDepartment;

    @Schema(description = "申请日期")
    private LocalDateTime applicationDate;

    @Schema(description = "申请理由", example = "不香")
    private String applicationReason;



    @Schema(description = "采购申请详情列表")
    @Valid
    @NotEmpty(message = "采购申请详情不能为空")
    private List<Detail> details;

    @Schema(description = "采购申请详情")
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Detail {

        @Schema(description = "主键", requiredMode = Schema.RequiredMode.REQUIRED, example = "123.000")
        private String id;

        @Schema(description = "源单类型", requiredMode = Schema.RequiredMode.REQUIRED, example = "123.000")
        // @NotNull(message = "源单类型不能为空")
        private Integer sourceType;

        @Schema(description = "源单ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "123.000")
        private String sourceId;

        @Schema(description = "需求物料", requiredMode = Schema.RequiredMode.REQUIRED, example = "20529")
        @NotEmpty(message = "需求物料不能为空")
        private String requiredMaterial;

        @Schema(description = "需求数量", requiredMode = Schema.RequiredMode.REQUIRED, example = "123.000")
        @NotNull(message = "需求数量不能为空")
        private BigDecimal requiredQuantity;

        @Schema(description = "需求时间", requiredMode = Schema.RequiredMode.REQUIRED, example = "123.00")
        private LocalDateTime requiredDate;

        @Schema(description = "预算单价", requiredMode = Schema.RequiredMode.REQUIRED, example = "123.00")
        private BigDecimal estimatedPrice;

        @Schema(description = "供应商", requiredMode = Schema.RequiredMode.REQUIRED, example = "123.00")
        private String supplier;

        @Schema(description = "订单id", requiredMode = Schema.RequiredMode.REQUIRED, example = "123.00")
        private String orderId;
        @Schema(description = "计划id", requiredMode = Schema.RequiredMode.REQUIRED, example = "123.00")
        private String projectPlanId;
        @Schema(description = "子计划id", requiredMode = Schema.RequiredMode.REQUIRED, example = "123.00")
        private String projectPlanItemId;
        @Schema(description = "项目id", requiredMode = Schema.RequiredMode.REQUIRED, example = "123.00")
        private String projectId;
        @Schema(description = "计划类型", requiredMode = Schema.RequiredMode.REQUIRED, example = "123.00")
        private Integer planType;
    }
}