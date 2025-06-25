package com.miyu.module.ppm.dal.dataobject.shippinginstoragedetail;

import lombok.*;
import java.util.*;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import java.math.BigDecimal;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.*;
import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;

/**
 * 销售订单入库明细 DO
 *
 * @author 上海弥彧
 */
@TableName("dm_shipping_instorage_detail")
@KeySequence("dm_shipping_instorage_detail_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ShippingInstorageDetailDO extends BaseDO {

    /**
     * ID
     */
    @TableId(type = IdType.ASSIGN_ID)
    private String id;
    /**
     * 收货单ID
     */
    private String shippingStorageId;

    /***
     * 物料类型ID
     */
    private String materialId;
    /**
     * 项目ID
     */
    private String projectId;
    /**
     * 项目订单ID
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


    private Integer status;

}