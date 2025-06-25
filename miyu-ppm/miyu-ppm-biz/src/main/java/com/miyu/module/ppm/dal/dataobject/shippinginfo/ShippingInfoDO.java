package com.miyu.module.ppm.dal.dataobject.shippinginfo;

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
 * 销售发货产品 DO
 *
 * @author 上海弥彧
 */
@TableName("dm_shipping_info")
@KeySequence("dm_shipping_info_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ShippingInfoDO extends BaseDO {

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
     * 合同ID
     */
    private String projectId;
    /***
     * 项目订单ID
     */
    private String projectOrderId;
    /**
     * 合同订单ID
     */
    private String orderId;
    /**
     * 合同ID
     */
    private String contractId;
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
     * 物料类型
     */
    private String materialConfigId;
    /**
     * 状态  0已创建 1审批中 2 待出库 3出库中4发货确认5结束 9作废8审批失败
     */
    private Integer shippingStatus;

    /***
     * 发货单类型
     * 1销售发货2外协发货3采购退货4委托加工退货
     */
    private Integer shippingType;


    private String projectPlanId;
    private String projectPlanItemId;
    private String consignmentId;


    /***
     * 出库状态
     * 出库状态1待出库 2待签收
     */
    private Integer outStatus;
}