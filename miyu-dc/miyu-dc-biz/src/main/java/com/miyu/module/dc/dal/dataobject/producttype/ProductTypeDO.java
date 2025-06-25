package com.miyu.module.dc.dal.dataobject.producttype;

import com.miyu.module.dc.dal.dataobject.collectattributes.CollectAttributesDO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.math.BigDecimal;
import java.util.*;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.*;
import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;

/**
 * 产品类型 DO
 *
 * @author 芋道源码
 */
@TableName("dc_product_type")
@KeySequence("dc_product_type_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductTypeDO extends BaseDO {

    /**
     * 产品类型id
     */
    @TableId(type = IdType.ASSIGN_ID)
    private String Id;
    /**
     * 产品类型名称
     */
    private String productTypeName;
    /**
     * 产品类型介绍
     */
    private String productTypeText;

    private String topicId;

    /**
     * 采集周期
     */
    private BigDecimal collectAttributesCycle;

    /**
     * 采集方式
     */
    private Integer collectAttributesType;

    /**
     * 采集属性
     */
    @TableField(exist = false)
    private List<CollectAttributesDO> collectAttributesDetails;

    /**
     * 采集属性格式
     */
    @TableField(exist = false)
    private String TypeCode;

    /**
     * 在线状态(1.在线 2.异常 0.未接入平台)
     */
    @TableField(exist = false)
    private Integer onlineStatus;

    /**
     * 标准值状态(1.在线 2.异常 0.未接入平台)
     */
    @TableField(exist = false)
    private Integer normStatus;


}