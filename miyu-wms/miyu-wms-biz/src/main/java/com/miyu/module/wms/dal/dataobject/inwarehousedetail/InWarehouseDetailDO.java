package com.miyu.module.wms.dal.dataobject.inwarehousedetail;

import lombok.*;
import java.util.*;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.*;
import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;

/**
 * 入库详情 DO
 *
 * @author QianJy
 */
@TableName("wms_in_warehouse_detail")
@KeySequence("wms_in_warehouse_detail_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InWarehouseDetailDO extends BaseDO {

    /**
     * ID
     */
    @TableId(type = IdType.ASSIGN_ID)
    private String id;
    /**
     * 入库单号 默认为来源单号；自建单则自动生成
     */
    private String orderNumber;
    /**
     * 入库类型（采购入库、外协入库、生产入库、退料入库、检验入库、其他入库）
     *
     * 枚举 {@link TODO wms_in_type 对应的类}
     */
    private Integer inType;
    /**
     * 起始仓库id  没啥用  冗余字段 进行展示和追溯
     */
    private String startWarehouseId;
    /**
     * 入库仓库ID
     */
    private String targetWarehouseId;
    /**
     * 入库状态（待质检、待入库、待上架、已完成、已关闭）
     *
     * 枚举 {@link TODO wms_in_state_detail 对应的类}
     */
    private Integer inState;
    /**
     * 物料批次号(冗余方便查询)
     */
    private String batchNumber;
    /**
     * 物料类型id(冗余方便查询)
     */
    private String materialConfigId;
    /**
     * 物料id
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
     * 签收人 (人工库才需要签收)
     */
    private String signer;
    /**
     * 签收时间
     */
    private LocalDateTime signTime;

    @TableField(exist = false)
    private String materialNumber;
    @TableField(exist = false)
    private String chooseBarCode;
    @TableField(exist = false)
    private String realBarCode;
    @TableField(exist = false)
    private String warehouseCode;
    @TableField(exist = false)
    private String startWarehouseCode;

}