package com.miyu.module.wms.dal.dataobject.movewarehousedetail;

import lombok.*;
import com.baomidou.mybatisplus.annotation.*;
import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;

import java.time.LocalDateTime;

/**
 * 库存移动详情 DO
 *
 * @author QianJy
 */
@TableName("wms_move_warehouse_detail")
@KeySequence("wms_move_warehouse_detail_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MoveWarehouseDetailDO extends BaseDO {

    /**
     * ID
     */
    @TableId(type = IdType.ASSIGN_ID)
    private String id;
    /**
     * 移库单号
     */
    private String orderNumber;
    /**
     * 移库类型（生产移库，检验移库，调拨移库）
     *
     * 枚举 {@link TODO wms_out_type 对应的类}
     */
    private Integer moveType;
    /**
     * 移库状态（待移交、待送达、待签收、已完成、已关闭）
     *
     * 枚举 {@link TODO wms_out_state 对应的类}
     */
    private Integer moveState;
    /**
     * 起始仓库id
     */
    private String startWarehouseId;
    /**
     * 目标仓库id
     */
    private String targetWarehouseId;
    /**
     * 签收库位id
     */
    private String signLocationId;
    /**
     * 物料批次号(冗余方便查询)
     */
    private String batchNumber;
    /**
     * 物料类型id(冗余方便查询)
     */
    private String materialConfigId;
    /**
     * 物料库存id
     */
    private String materialStockId;
    /**
     * 选择物料id
     */
    private String chooseStockId;
    /**
     * 数量
     */
    private Integer quantity;
    /**
     * 搬运托盘id
     */
    private String carryTrayId;
    /**
     * 操作人
     */
    private String operator;
    /**
     * 操作时间
     */
    private LocalDateTime operateTime;
    /**
     * 签收人
     */
    private String signer;
    /**
     * 签收时间
     */
    private LocalDateTime signTime;



    @TableField(exist = false)
    private String materialNumber;
    @TableField(exist = false)
    private String realBarCode;
    @TableField(exist = false)
    private String chooseBarCode;
    @TableField(exist = false)
    private String startWarehouseCode;
    @TableField(exist = false)
    private String targetWarehouseCode;

    /**
     * 总库存数量
     */
    @TableField(exist = false)
    private Integer totality;
    @TableField(exist = false)
    private Integer locked;

}