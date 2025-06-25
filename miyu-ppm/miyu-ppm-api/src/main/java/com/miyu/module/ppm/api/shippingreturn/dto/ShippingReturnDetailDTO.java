package com.miyu.module.ppm.api.shippingreturn.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Schema(description = "RPC 服务 - 销售系统  退货单明细")
@Data
public class ShippingReturnDetailDTO {

    /**
     * ID
     */
    private String id;
    /**
     * 退货单ID
     */
    private String consignmentId;
    /***
     * 发货单详情ID
     */
    private String shippingDetailId;
    /**
     * 合同订单ID
     */
    private String orderId;
    /**
     * 退货数量
     */
    private BigDecimal consignedAmount;
    /**
     * 入库数量
     */
    private BigDecimal inboundAmount;
    /**
     * 入库人
     */
    private String inboundBy;
    /**
     * 出库时间
     */
    private LocalDateTime inboundTime;
    /**
     * 确认数量
     */
    private BigDecimal signedAmount;
    /**
     * 确认人
     */
    private String signedBy;
    /**
     * 确认日期
     */
    private LocalDateTime signedTime;
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


    /***
     * 发货单
     */
    private String no;
    /***
     * 发货单名称
     */
    private String name;
}
