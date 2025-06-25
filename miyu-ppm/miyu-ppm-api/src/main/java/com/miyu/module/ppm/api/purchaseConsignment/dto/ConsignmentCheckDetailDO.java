package com.miyu.module.ppm.api.purchaseConsignment.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Schema(description = "管理后台 - 采购收货明细-免检 Request VO")
public class ConsignmentCheckDetailDO {

    /**
     * 采购收货数据ID
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
     * 产品ID
     */
    private String materialId;
    /**
     * 产品所属企业ID
     */
    private String companyId;
    /**
     * 产品所属名称ID
     */
    private String companyName;
    /**
     * 初始单价
     */
    private BigDecimal initPrice;
    /**
     * 初始税率
     */
    private String initTax;
    /**
     * 供货周期
     */
    private int leadTime;
    /**
     * 是否免检
     */
    private int qualityCheck;
    /**
     * 产品名称
     */
    private String materialName;




}
