package com.miyu.module.ppm.controller.admin.contractpayment.vo;

import cn.iocoder.yudao.framework.common.pojo.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Schema(description = "管理后台 - 合同付款 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class ContractPaymentReqVO extends PageParam {

    @Schema(description = "主键")
    private String paymentId;

    @Schema(description = "合同ID")
    private String contractId;

    @Schema(description = "合同发票主键")
    private String invoiceId;
}