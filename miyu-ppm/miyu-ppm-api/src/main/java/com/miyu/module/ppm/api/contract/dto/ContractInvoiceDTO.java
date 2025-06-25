package com.miyu.module.ppm.api.contract.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 购销合同发票 DTO
 *
 * @author Zhangyunfei
 */
@Data
@Schema(description = "合同发票")
public class ContractInvoiceDTO {

    /**
     * 主键
     */
    private String id;
    /**
     * 合同ID
     */
    private String contractId;
    /**
     * 业务类型1采购 2销售
     */
    private Integer businessType;
    /**
     * 类型，普票、专票、收据等
     */
    private Integer type;
    /**
     * 金额
     */
    private BigDecimal amount;
    /**
     * 开具时间
     */
    private LocalDateTime invoiceDate;
    /**
     * 票据代码
     */
    private String invoiceNumber;
    /**
     * 票据代码2
     */
    private String invoiceNumber2;

    /**
     * 附件地址
     */
    private String fileUrl;

    /**
     * 审批状态
     */
    private Integer status;

    private String contractNumber;

    private String contractName;

}