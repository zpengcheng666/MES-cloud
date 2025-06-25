package com.miyu.module.ppm.api.consignmentReturn.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Schema(description = "管理后台 - 采购退货单明细")
@Data
public class ConsignmentReturnDetailDTO {

    /**
     * ID
     */
    private String id;
    /**
     * 退货单ID
     */
    private String shippingId;
    /**
     * 退货数量
     */
    private BigDecimal consignedAmount;
    /**
     * 出库数量
     */
    private BigDecimal inboundAmount;
    /**
     * 出库人
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
    private String  materialStockId;

    /**
     * 物料条码
     */
    private String barCode;

    /**
     * 物料批次号
     */
    private String batchNumber;

    /**
     * 收货单Id
     */
    private String consignmentId;

    /**
     * 收货单详情Id
     */
    private String consignmentDetailId;

    /**
     *  物料类型
     */
    private String materialConfigId;

    /**
     * 收货单名称
     */
    private String consignmentName;

    /**
     *  物料类型ID
     */
    private String materialId;

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
     * 库存数量
     */
    private BigDecimal quantity;


}
