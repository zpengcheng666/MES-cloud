package com.miyu.module.ppm.api.consignmentReturn.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Schema(description = "管理后台 - 采购退货单")
@Data
public class ReturnMaterialDTO {
    /**
     * ID
     */
    private String id;
    /**
     * 发货单ID
     */
    private String shippingId;
    /**
     * 项目ID
     */
    private String projectId;
    /***
     * 项目订单ID
     */
    private String projectOrderId;


    private String projectPlanId;


    private String projectPlanItemId;
    /**
     * 项目订单ID
     */
    private String orderId;
    /***
     * 合同ID
     */
    private String contractId;

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


    /***
     * 物料类型ID
     */
    private String materialConfigId;

    /***
     * 编号
     */

    private String no;
    /***
     * 名称
     */

    private String name;

    /***
     * 状态
     */
    private Integer shippingStatus;

    /***
     * 发货产品表Id
     */
    private String infoId;

    /***
     * 发货单类型
     * 1销售发货2外协发货3采购退货4委托加工退货
     */
    private String shippingType;

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

    private String consignmentId;
    private String consignmentDetailId;
}
