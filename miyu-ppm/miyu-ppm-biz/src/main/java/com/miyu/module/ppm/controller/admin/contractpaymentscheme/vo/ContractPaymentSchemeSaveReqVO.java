package com.miyu.module.ppm.controller.admin.contractpaymentscheme.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import javax.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Schema(description = "管理后台 - 合同付款计划新增/修改 Request VO")
@Data
public class ContractPaymentSchemeSaveReqVO {

    @Schema(description = "主键", requiredMode = Schema.RequiredMode.REQUIRED, example = "14395")
    private String id;

    @Schema(description = "合同ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "23198")
    @NotEmpty(message = "合同ID不能为空")
    private String contractId;

    @Schema(description = "付款控制，货到付款、预付、货到票据支付、货到分期、货到延期等", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "付款控制，货到付款、预付、货到票据支付、货到分期、货到延期等不能为空")
    private Integer paymentControl;

    @Schema(description = "付款日期", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "付款日期不能为空")
    private LocalDateTime payDate;

    @Schema(description = "比例", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "比例不能为空")
    private BigDecimal ratio;

    @Schema(description = "金额，与比例联动", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "金额，与比例联动不能为空")
    private BigDecimal amount;

    @Schema(description = "付款方式，银行汇款、银行本票、商业汇票、现金、托收承付等", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "付款方式，银行汇款、银行本票、商业汇票、现金、托收承付等不能为空")
    private Integer method;

    @Schema(description = "备注", example = "随便")
    private String remark;

}