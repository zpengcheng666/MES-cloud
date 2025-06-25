package com.miyu.module.ppm.api.purchaseConsignment.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Schema(description = "RPC 服务 - 采购系统  采购收货详情")
@Data
public class PurchaseConsignmentDetailDTO{

    /**
     * ID
     */
    private String id;
    /**
     * 收货单ID
     */
    private String consignmentId;
    /**
     * 合同订单ID
     */
    private String orderId;
    /**
     * 发货数量
     */
    private BigDecimal consignedAmount;
    /**
     * 签收数量
     */
    private BigDecimal signedAmount;
    /**
     * 签收人
     */
    private String signedBy;
    /**
     * 签收日期
     */
    private LocalDateTime signedTime;

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


    /**
     * 物料类型Id
     */
    private String materialId;

    private String materialStockId;
    private String barCode;
    private String batchNumber;
    private String materialConfigId;
    private String consignmentType;
    private Integer consignmentStatus;

    private String projectId;
    private String projectOrderId;
    private String projectPlanId;
    private String projectPlanItemId;

    private String shippingId;
    private String shippingDetailId;
    /** 采购计划id,关联数据用 */
    private String purchaseId;


}
