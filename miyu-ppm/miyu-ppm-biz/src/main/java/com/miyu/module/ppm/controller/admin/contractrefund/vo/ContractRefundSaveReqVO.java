package com.miyu.module.ppm.controller.admin.contractrefund.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import javax.validation.constraints.*;
import java.math.BigDecimal;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;

@Schema(description = "管理后台 - 合同退款新增/修改 Request VO")
@Data
public class ContractRefundSaveReqVO {

    @Schema(description = "主键", requiredMode = Schema.RequiredMode.REQUIRED, example = "15419")
    private String id;

    @Schema(description = "退货单", example = "32569")
    private String shippingReturnId;
    @Schema(description = "退款单号", example = "32569")
    private String no;
    @Schema(description = "合同ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "16435")
    @NotEmpty(message = "合同ID不能为空")
    private String contractId;

    @Schema(description = "退款方式现金、承兑、信用证、支付宝、二维码等", example = "1")
    private Integer refundType;

    @Schema(description = "退款日期")
    private LocalDateTime refundTime;

    @Schema(description = "退款金额", example = "22579")
    private BigDecimal refundPrice;

}