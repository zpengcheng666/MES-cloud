package com.miyu.module.ppm.controller.admin.contractpayment.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import javax.validation.Valid;
import javax.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Schema(description = "管理后台 - 合同付款新增/修改 Request VO")
@Data
public class ContractPaymentSaveReqVO {

    @Schema(description = "主键", requiredMode = Schema.RequiredMode.REQUIRED, example = "28123")
    private String id;

    @Schema(description = "合同ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "27607")
    @NotEmpty(message = "合同ID不能为空")
    private String contractId;

    @Schema(description = "税务信息ID", example = "27389")
    private String financeId;

    @Schema(description = "业务类型1采购 2销售", example = "1")
    private Integer businessType;

    @Schema(description = "实际付款日期", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "实际付款日期不能为空")
    private LocalDateTime payDate;

    @Schema(description = "实际付款金额", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "实际付款金额不能为空")
    private BigDecimal amount;

    @Schema(description = "实际付款方式", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "实际付款方式不能为空")
    private Integer method;

    @Schema(description = "付款凭证")
    private String evidence;

    @Schema(description = "备注", example = "你猜")
    private String remark;

    @Schema(description = "流程key")
    private String processKey;

    @Schema(description = "合同付款详细列表")
    @Valid
    @NotEmpty(message = "合同付款详细不能为空")
    private List<PaymentDetail> paymentDetails;

    @Schema(description = "付款计划详细列表")
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PaymentDetail {

        @Schema(description = "付款计划", requiredMode = Schema.RequiredMode.REQUIRED, example = "27389")
        @NotNull(message = "付款计划不能为空")
        private String schemeId;

        @Schema(description = "金额", requiredMode = Schema.RequiredMode.REQUIRED, example = "123.00")
        @NotNull(message = "金额不能为空")
        private BigDecimal amount;

    }
}