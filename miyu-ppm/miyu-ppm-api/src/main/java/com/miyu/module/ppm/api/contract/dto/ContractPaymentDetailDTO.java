package com.miyu.module.ppm.api.contract.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Schema(description = "合同付款明细")
public class ContractPaymentDetailDTO {

    private String id;
    /**
     * 合同ID
     */
    private String paymentId;
    /**
     * 付款计划ID
     */
    private String schemeId;
    /**
     * 实际付款金额
     */
    private BigDecimal amount;
    /**
     * 工作流编号
     */
    private String processInstanceId;

    /**
     * 付款计划表数据
     */

    // 打款方式
    private Integer paymentControl;

    // 付款日期
    private LocalDateTime payDate;

    // 比例
    private BigDecimal ratio;

    // 金额
    private BigDecimal schemeAmount;

    // 付款方式
    private Integer method;

    // 备注
    private String remark;

    // 剩余支付金额
    private BigDecimal remainAmount;
}
