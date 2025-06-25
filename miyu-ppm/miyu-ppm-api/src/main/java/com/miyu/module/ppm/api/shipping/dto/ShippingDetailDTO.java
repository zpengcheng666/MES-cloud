package com.miyu.module.ppm.api.shipping.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Schema(description = "RPC 服务 - 销售系统  发货单明细")
@Data
public class ShippingDetailDTO {

    /**
     * ID
     */
    private String id;
    /**
     * 发货单ID
     */
    private String shippingId;
    /**
     * 合同ID
     */
    private String contractId;
    /**
     * 合同订单ID
     */
    private String orderId;

    /**
     * 物料库存ID
     */
    private String materialStockId;
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
     * 出库人
     */
    private LocalDateTime outboundTime;
    /**
     * 确认数量
     */
    private BigDecimal signedAmount;
    /**
     * 确认
     */
    private String signedBy;
    /**
     * 确认日期
     */
    private LocalDateTime signedTime;



    /***
     * 发货单
     */
    private String shippingNo;
    /***
     * 发货单名称
     */
    private String shippingName;

    private Integer shippingStatus;
    private Integer shippingType;

    private String projectOrderId;



    private String materialConfigId;

    /**
     * 物料编号
     */
    private String materialNumber;

    /**
     * 物料名称
     */
    private String materialName;

    /**
     * 物料单位
     */
    private String materialUnit;

    private String consignmentId;
    private String consignmentDetailId;
}
