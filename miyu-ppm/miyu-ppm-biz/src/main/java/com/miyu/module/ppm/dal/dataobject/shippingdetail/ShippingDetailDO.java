package com.miyu.module.ppm.dal.dataobject.shippingdetail;

import lombok.*;

import java.util.*;
import java.math.BigDecimal;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import java.time.LocalDateTime;

import com.baomidou.mybatisplus.annotation.*;
import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;

/**
 * 销售发货明细 DO
 *
 * @author 芋道源码
 */
@TableName("dm_shipping_detail")
@KeySequence("dm_shipping_detail_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ShippingDetailDO extends BaseDO {

    /**
     * ID
     */
    @TableId(type = IdType.ASSIGN_ID)
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
     * 发货单
     */
    @TableField(exist = false)
    private String no;
    /***
     * 发货单名称
     */
    @TableField(exist = false)
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
    private Integer shippingType;





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