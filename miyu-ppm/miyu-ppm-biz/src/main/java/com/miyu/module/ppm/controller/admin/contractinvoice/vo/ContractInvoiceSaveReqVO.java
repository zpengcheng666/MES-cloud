package com.miyu.module.ppm.controller.admin.contractinvoice.vo;

import com.miyu.module.ppm.controller.admin.contractpayment.vo.ContractPaymentSaveReqVO;
import com.miyu.module.ppm.dal.dataobject.contractinvoice.ContractInvoiceDetailDO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import javax.validation.Valid;
import javax.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Schema(description = "管理后台 - 购销合同发票新增/修改 Request VO")
@Data
public class ContractInvoiceSaveReqVO {

    @Schema(description = "主键", requiredMode = Schema.RequiredMode.REQUIRED, example = "23687")
    private String id;

    @Schema(description = "合同ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "2818")
    @NotEmpty(message = "合同ID不能为空")
    private String contractId;

    @Schema(description = "业务类型1采购 2销售", example = "1")
    private Integer businessType;

    @Schema(description = "类型，普票、专票、收据等", requiredMode = Schema.RequiredMode.REQUIRED, example = "2")
    @NotNull(message = "类型，普票、专票、收据等不能为空")
    private Integer type;

    @Schema(description = "金额", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "金额不能为空")
    private BigDecimal amount;

    @Schema(description = "开具时间")
    private LocalDateTime invoiceDate;

    @Schema(description = "票据代码")
    private String invoiceNumber;

    @Schema(description = "票据代码2")
    private String invoiceNumber2;

    @Schema(description = "附件地址", example = "https://www.iocoder.cn")
    private String fileUrl;

    @Schema(description = "流程key")
    private String processKey;

//    @Schema(description = "购销合同发票表详细列表")
//    private List<ContractInvoiceDetailDO> contractInvoiceDetails;

    @Schema(description = "合同发票详细列表")
    @Valid
    private List<InvoiceDetail> invoiceDetails;

    @Schema(description = "合同发票详细列表")
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class InvoiceDetail {

        @Schema(description = "付款记录", requiredMode = Schema.RequiredMode.REQUIRED, example = "27389")
        @NotNull(message = "付款记录不能为空")
        private String paymentId;

        @Schema(description = "开具金额", requiredMode = Schema.RequiredMode.REQUIRED, example = "123.00")
        @NotNull(message = "开具金额不能为空")
        private BigDecimal amount;

    }

}