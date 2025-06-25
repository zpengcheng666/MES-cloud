package com.miyu.module.wms.dal.dataobject.warehouse;

import com.alibaba.excel.annotation.ExcelProperty;
import com.miyu.module.wms.dal.dataobject.warehousearea.WarehouseAreaDO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.*;
import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;

/**
 * 仓库表 DO
 *
 * @author Qianjy
 */
@TableName("wms_warehouse")
@KeySequence("wms_warehouse_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WarehouseDO extends BaseDO {

    /**
     * ID
     */
    @TableId(type = IdType.ASSIGN_ID)
    private String id;
    /**
     * 仓库名称
     */
    private String warehouseName;
    /**
     * 仓库编码
     */
    private String warehouseCode;
    /**
     * 仓库地址
     */
    private String warehouseAddress;
    /**
     * 仓库容量
     */
    private String warehouseCapacity;
    /**
     * 仓库性质
     */
    private Integer warehouseNature;
    /**
     * 仓库类型
     */
    private Integer warehouseType;
    /**
     * 仓库状态
     */
    private Integer warehouseState;
    /**
     * 仓库主管
     */
    private String userId;

    /**
     * 类型：0根节点，1仓库，2库区，3库位
     */
    @TableField(exist = false)
    private Integer type = 1;

    /**
     * 节点名字
     */
    @TableField(exist = false)
    private String name;
    /**
     * 库区列表
     */
    @TableField(exist = false)
    private List<WarehouseAreaDO> childrens;

}