package com.miyu.module.wms.dal.dataobject.materialstorage;

import lombok.*;
import com.baomidou.mybatisplus.annotation.*;
import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;

/**
 * 物料储位 DO
 *
 * @author QianJy
 */
@TableName("wms_material_storage")
@KeySequence("wms_material_storage_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MaterialStorageDO extends BaseDO {

    /**
     * ID
     */
    @TableId(type = IdType.ASSIGN_ID)
    private String id;
    /**
     * 储位编码
     */
    private String storageCode;
    /**
     * 储位编码
     */
    private String storageName;
    /**
     * 物料id
     */
    private String materialStockId;

    /**
     * 是否有效
     */
    private Integer valid;
    /**
     * 层
     */
    private Integer layer;
    /**
     * 排
     */
    private Integer row;
    /**
     * 列
     */
    private Integer col;

    /**
     * 物料条码
     */
    @TableField(exist = false)
    private String barCode;

    /**
     * 节点名字
     */
    @TableField(exist = false)
    private String name;

    /**
     * 类型：0根节点，1容器，2储位
     */
    @TableField(exist = false)
    private Integer type = 2;
}