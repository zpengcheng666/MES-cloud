package com.miyu.module.ppm.api.shipping.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Schema(description = "RPC 服务 - 销售系统  发货单明细(追溯用)")
@Data
public class ShippingDetailRetraceDTO {


    /**
     * 合同ID
     */
    private String contractName;
    /***
     * 合同编号
     */
    private String contractNo;
    /***
     * 供应商
     */
    private String companyName;
    /***
     * 发货单编号
     */
    private String shippingNo;
    /***
     * 发货单名称
     */
    private String shippingName;
    /**
     * 物料条码
     */
    private String barCode;
    /**
     * 物料批次号
     */
    private String batchNumber;
    /**
     * 发货数量
     */
    private BigDecimal consignedAmount;
    /**
     * 出库数量
     */
    private BigDecimal outboundAmount;
    /**
     * 出库人
     */
    private String outboundBy;
    /**
     * 出库时间
     */
    private LocalDateTime outboundTime;
    /**
     * 确认人
     */
    private String signedBy;
    /**
     * 确认日期
     */
    private LocalDateTime signedTime;


    private LocalDateTime createTime;



}
