package com.miyu.module.wms.dal.dataobject.materialconfigarea;

import lombok.*;
import java.util.*;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.*;
import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;

/**
 * 物料类型关联库区配置 DO
 *
 * @author QianJy
 */
@TableName("wms_material_config_area")
@KeySequence("wms_material_config_area_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MaterialConfigAreaDO extends BaseDO {

    /**
     * ID
     */
    @TableId(type = IdType.ASSIGN_ID)
    private String id;
    /**
     * 库区编码
     */
    private String warehouseAreaId;

    @TableField(exist = false)
    private String areaCode;
    /**
     * 物料类型
     */
    private String materialConfigId;

    @TableField(exist = false)
    private String materialNumber;

}