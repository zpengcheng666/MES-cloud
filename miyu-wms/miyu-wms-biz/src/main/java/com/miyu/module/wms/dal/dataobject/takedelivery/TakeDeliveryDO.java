package com.miyu.module.wms.dal.dataobject.takedelivery;

import lombok.*;
import java.util.*;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.*;
import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;

/**
 * 物料收货 DO
 *
 * @author QianJy
 */
@TableName("wms_take_delivery")
@KeySequence("wms_take_delivery_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TakeDeliveryDO extends BaseDO {

    /**
     * ID
     */
    @TableId(type = IdType.ASSIGN_ID)
    private String id;
    /**
     * 到货单号
     */
    private String orderNumber;
    /**
     * 物料类型id
     */
    private String materialConfigId;
    /**
     * 收货数量
     */
    private Integer tdQuantity;
    /**
     * 绑定库位
     */
    private String locationId;
    /**
     * 绑定储位
     */
    private String storageId;
    /**
     * 绑定物料
     */
    private String materialId;

}