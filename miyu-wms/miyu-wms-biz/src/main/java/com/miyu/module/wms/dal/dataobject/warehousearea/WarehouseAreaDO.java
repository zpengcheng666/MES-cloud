package com.miyu.module.wms.dal.dataobject.warehousearea;

import com.miyu.module.wms.dal.dataobject.warehouselocation.WarehouseLocationDO;
import lombok.*;
import com.baomidou.mybatisplus.annotation.*;
import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;

import java.util.List;

/**
 * 库区 DO
 *
 * @author QianJy
 */
@TableName("wms_warehouse_area")
@KeySequence("wms_warehouse_area_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WarehouseAreaDO extends BaseDO {

    /**
     * ID
     */
    @TableId(type = IdType.ASSIGN_ID)
    private String id;
    /**
     * 仓库id
     */
    private String warehouseId;
    /**
     * 库区名称
     */
    private String areaName;
    /**
     * 库区编码
     */
    private String areaCode;
    /**
     * 库区属性
     *
     * 枚举 {@link TODO wms_warehouse_area_property 对应的类}
     */
    private Integer areaProperty;
    /**
     * 库区长
     */
    private Integer areaLength;
    /**
     * 库区宽
     */
    private Integer areaWidth;
    /**
     * 库区高
     */
    private Integer areaHeight;
    /**
     * 库区承重
     */
    private Integer areaBearing;
    /**
     * 通道
     */
    private Integer areaChannels;
    /**
     * 组
     */
    private Integer areaGroup;
    /**
     * 层
     */
    private Integer areaLayer;
    /**
     * 位
     */
    private Integer areaSite;
    /**
     * 库区类型  1存储区2暂存区3物料接驳区4拣选区5收货区6虚拟暂存区7打包区8质检区9机加功能区10加工缓存库区11刀具接驳区
     */
    private Integer areaType;
    /**
     * 刀具配送顺序
     */
    private Integer deliverySequence;

    // 库位id
    @TableField(exist = false)
    private String joinLocationId;

    /**
     * 类型：0根节点，1仓库，2库区，3库位
     */
    @TableField(exist = false)
    private Integer type = 2;
    /**
     * 节点名字
     */
    @TableField(exist = false)
    private String name;
    /**
     * 库位列表
     */
    @TableField(exist = false)
    private WarehouseLocationDO[][][][] childrens;
    /**
     * 库位数量
     */
    @TableField(exist = false)
    private Integer locationCount;

    @TableField(exist = false)
    private String warehouseCode;

    @TableField(exist = false)
    private String materialStockId;


}