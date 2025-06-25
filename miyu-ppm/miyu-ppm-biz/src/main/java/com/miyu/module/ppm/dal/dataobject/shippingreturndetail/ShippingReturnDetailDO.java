package com.miyu.module.ppm.dal.dataobject.shippingreturndetail;

import com.miyu.module.ppm.enums.shippingreturn.ShippingReturnTypeEnum;
import lombok.*;
import java.util.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.*;
import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;

/**
 * 销售退货单详情 DO
 *
 * @author miyudmA
 */
@TableName("dm_shipping_return_detail")
@KeySequence("dm_shipping_return_detail_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ShippingReturnDetailDO extends BaseDO {

    /**
     * ID
     */
    @TableId(type = IdType.ASSIGN_ID)
    private String id;
    /**
     * 退货单ID
     */
    private String shippingReturnId;
    /***
     * 发货单详情ID
     */
    private String shippingDetailId;
    /**
     * 项目订单ID
     */
    private String orderId;
    /***
     * 项目ID
     */
    private String projectId;
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

    private String materialConfigId;
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
    @TableField(exist = false)
    private String shippingNo;
    /***
     * 发货单名称
     */
    @TableField(exist = false)
    private String shippingName;


    /**
     * 退货单编号
     */
    @TableField(exist = false)
    private String shippingReturnNo;
    /**
     * 退货单名称
     */
    @TableField(exist = false)
    private String shippingReturnName;

    @TableField(exist = false)
    private String contractId;


    /**
     * 处理方式 1返修2换货3退货退款4仅退款
     *
     * 枚举 {@link ShippingReturnTypeEnum}
     */
    @TableField(exist = false)
    private Integer returnType;
    /**
     * 退换货原因
     */
    @TableField(exist = false)
    private String returnReason;
}