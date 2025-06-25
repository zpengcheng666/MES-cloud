package com.miyu.module.ppm.api.shippingreturn.dto;

import com.miyu.module.ppm.enums.shippingreturn.ShippingReturnTypeEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Schema(description = "RPC 服务 - 销售系统  退货单明细(追溯用)")
@Data
public class ShippingReturnDetailRetraceDTO {


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


    /**
     * 退货单编号
     */
    private String shippingReturnNo;
    /**
     * 退货单名称
     */
    private String shippingReturnName;
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
     * 处理方式 1返修2换货3退货退款4仅退款
     *
     * 枚举 {@link ShippingReturnTypeEnum}
     */
    private String returnTypeName;
    /**
     * 退换货原因
     */
    private String returnReason;


    private LocalDateTime createTime;

}
