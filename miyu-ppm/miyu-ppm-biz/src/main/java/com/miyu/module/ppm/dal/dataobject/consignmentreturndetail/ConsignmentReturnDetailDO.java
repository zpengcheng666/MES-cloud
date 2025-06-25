package com.miyu.module.ppm.dal.dataobject.consignmentreturndetail;

import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.*;
import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;

/**
 * 销售退货单详情 DO
 *
 * @author 芋道源码
 */
@TableName("ppm_consignment_return_detail")
@KeySequence("ppm_consignment_return_detail_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ConsignmentReturnDetailDO extends BaseDO {

    /**
     * ID
     */
    @TableId(type = IdType.ASSIGN_ID)
    private String id;
    /**
     * 退货单ID
     */
    private String consignmentReturnId;
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
     * 收货单
     */
    @TableField(exist = false)
    private String no;


    /**
     *  WMS物料类型
     */
    private String materialConfigId;

    /**
     * 收货单名称
     */
    @TableField(exist = false)
    private String name;

    /**
     * 库存数量
     */
    @TableField(exist = false)
    private BigDecimal quantity;

    /**
     * 物料名称
     */
    @TableField(exist = false)
    private String materialName;

    /**
     * 物料含税单价
     */
    @TableField(exist = false)
    private BigDecimal taxPrice;

    /**
     * 物料含税总价
     */
    @TableField(exist = false)
    private BigDecimal Price;


    /**
     * 物料名称
     */
    @TableField(exist = false)
    private String materialNumber;

    /**
     * 物料单位
     */
    @TableField(exist = false)
    private String materialUnit;


}