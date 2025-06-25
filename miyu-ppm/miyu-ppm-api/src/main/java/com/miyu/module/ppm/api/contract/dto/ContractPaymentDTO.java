package com.miyu.module.ppm.api.contract.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Schema(description = "合同付款")
public class ContractPaymentDTO {

    private String id;
    /**
     * 合同ID
     */
    private String contractId;
    /**
     * 付款计划ID
     */
    private String schemeId;

    /**
     * 税务信息ID
     */
    private String financeId;

    /**
     * 实际付款日期
     */
    private LocalDateTime payDate;
    /**
     * 实际付款金额
     */
    private BigDecimal amount;
    /**
     * 实际付款方式
     */
    private Integer method;
    /**
     * 付款凭证
     */
    private String evidence;

    /**
     * 审批状态
     */
    private Integer status;

    /**
     * 备注
     */
    private String remark;


    private String contractNumber;

    private String contractName;

    private List<ContractPaymentDetailDTO> paymentDetailDTOList;

}
