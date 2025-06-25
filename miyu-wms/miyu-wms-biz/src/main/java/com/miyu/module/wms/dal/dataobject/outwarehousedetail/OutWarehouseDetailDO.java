package com.miyu.module.wms.dal.dataobject.outwarehousedetail;

import lombok.*;
import java.util.*;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.*;
import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;

/**
 * 出库详情 DO
 *
 * @author Qianjy
 */
@TableName("wms_out_warehouse_detail")
@KeySequence("wms_out_warehouse_detail_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OutWarehouseDetailDO extends BaseDO {

    /**
     * ID
     */
    @TableId(type = IdType.ASSIGN_ID)
    private String id;
    /**
     * 出库单号
     */
    private String orderNumber;
    /**
     * 出库类型（销售出库、外协出库、生产出库、检验出库、报损出库、采购退货出库、调拨出库、其他出库）
     */
    private Integer outType;
    /**
     * 出库状态（待出库、待送达、待签收、已完成、已关闭）
     */
    private Integer outState;
    /**
     * 出库仓库id  没啥用  冗余字段 进行展示和追溯
     */
    private String startWarehouseId;
    /**
     * 目标仓库id
     */
    private String targetWarehouseId;
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
    /**
     * 签收库位id
     */
    private String signLocationId;
    /**
     * 刀具需求位置id
     */
    private String needLocationId;
    /**
     * 需求时间
     */
    private LocalDateTime needTime;

    /**
     * 刀具配送顺序
     */
    private Integer deliverySequence;


    @TableField(exist = false)
    private String materialNumber;
    @TableField(exist = false)
    private String chooseBarCode;
    @TableField(exist = false)
    private String realBarCode;
    @TableField(exist = false)
    private String warehouseCode;
    @TableField(exist = false)
    private String targetWarehouseCode;


}