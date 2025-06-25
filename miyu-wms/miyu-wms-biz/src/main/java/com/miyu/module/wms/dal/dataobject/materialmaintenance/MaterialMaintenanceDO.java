package com.miyu.module.wms.dal.dataobject.materialmaintenance;

import lombok.*;
import java.util.*;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.*;
import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;

/**
 * 物料维护记录 DO
 *
 * @author QianJy
 */
@TableName("wms_material_maintenance")
@KeySequence("wms_material_maintenance_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MaterialMaintenanceDO extends BaseDO {

    /**
     * ID
     */
    @TableId(type = IdType.ASSIGN_ID)
    private String id;
    /**
     * 物料库存id
     */
    private String materialStockId;
    /**
     * 描述
     */
    private String description;
    /**
     * 数量
     */
    private Integer quantity;
    /**
     * 维护类型(1报废，2装配，3加工)
     *
     * 枚举 {@link TODO material_maintenance_type 对应的类}
     */
    private Integer type;


    @TableField(exist = false)
    private String barCode;
    @TableField(exist = false)
    private String materialNumber;
    @TableField(exist = false)
    private String materialName;

}