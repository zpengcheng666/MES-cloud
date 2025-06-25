package com.miyu.module.ppm.api.contract.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Schema(description = "合同付款计划")
public class ContractPaymentSchemeDTO {

    @Schema(description = "结算方式", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    private Integer paymentControl;

    @Schema(description = "付款日期", requiredMode = Schema.RequiredMode.REQUIRED)
    private LocalDateTime payDate;

    @Schema(description = "比例", requiredMode = Schema.RequiredMode.REQUIRED, example = "123.00")
    private BigDecimal ratio;

    @Schema(description = "金额", requiredMode = Schema.RequiredMode.REQUIRED, example = "123.00")
    private BigDecimal amount;

    @Schema(description = "付款方式", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    private Integer method;

    @Schema(description = "备注")
    private String remark;
}
