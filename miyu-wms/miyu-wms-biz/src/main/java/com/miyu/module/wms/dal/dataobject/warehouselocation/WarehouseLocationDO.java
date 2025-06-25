package com.miyu.module.wms.dal.dataobject.warehouselocation;

import com.miyu.module.wms.dal.dataobject.checkcontainer.CheckContainerDO;
import lombok.*;
import com.baomidou.mybatisplus.annotation.*;
import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;

/**
 * 库位 DO
 *
 * @author QianJy
 */
@TableName("wms_warehouse_location")
@KeySequence("wms_warehouse_location_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WarehouseLocationDO extends BaseDO {

    /**
     * ID
     */
    @TableId(type = IdType.ASSIGN_ID)
    private String id;
    /**
     * 库位编码
     */
    private String locationCode;

    private String locationName;
    /**
     * 库区id
     */
    private String warehouseAreaId;

    /**
     * 是否锁定
     */
    private Integer locked;

    /**
     * 是否有效
     */
    private Integer valid;
    /**
     * 通道
     */
    private Integer channel;
    /**
     * 组 (关键字)
     */
    private Integer groupp;
    /**
     * 层
     */
    private Integer layer;
    /**
     * 位
     */
    private Integer site;
    /**
     * 刀具配送顺序
     */
    private Integer deliverySequence;
    /**
     * 是否能承载托盘（用于接驳库位判定托盘是否可承载，是否需要生成默认回库的任务）
     */
    private Boolean isTray;

    /**
     * 类型：0根节点，1仓库，2库区，3库位
     */
    @TableField(exist = false)
    private Integer type = 3;
    /**
     * 仓库Id
     */
    @TableField(exist = false)
    private String warehouseId;

    /**
     * 仓库编码
     */
    @TableField(exist = false)
    private String warehouseCode;

    /**
     * 仓库名称
     */
    @TableField(exist = false)
    private String warehouseName;

    /**
     * 节点名字
     */
    @TableField(exist = false)
    private String name;

    /**
     * 库区名称
     */
    @TableField(exist = false)
    private String areaName;

    /**
     * 库位编码
     */
    @TableField(exist = false)
    private String areaCode;

    /**
     * 库区属性
     */
    @TableField(exist = false)
    private Integer areaProperty;

    /**
     * 库区类型
     */
    @TableField(exist = false)
    private Integer areaType;

    @TableField(exist = false)
    private CheckContainerDO checkContainer;
}