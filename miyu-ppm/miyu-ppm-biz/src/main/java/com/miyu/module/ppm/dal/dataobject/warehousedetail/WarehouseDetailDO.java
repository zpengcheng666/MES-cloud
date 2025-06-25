package com.miyu.module.ppm.dal.dataobject.warehousedetail;

import lombok.*;

import java.math.BigDecimal;
import java.util.*;
import com.baomidou.mybatisplus.annotation.*;
import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;

/**
 * 入库详情表 对应仓库库存 来源WMS DO
 *
 * @author 芋道源码
 */
@TableName("ppm_warehouse_detail")
@KeySequence("ppm_warehouse_detail_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WarehouseDetailDO extends BaseDO {

    /**
     * ID
     */
    @TableId(type = IdType.ASSIGN_ID)
    private String id;
    /**
     * 入库单号(对应收货单号)
     */
    private String orderNumber;
    /**
     * 入库仓库Id
     */
    private String warehouseId;
    /**
     * 入库状态(1.待质检 2.待入库 3.待上架 4.已完成 5.已关闭)
     *
     * 枚举 {@link TODO wms_in_state 对应的类}
     */
    private Integer instate;
    /**
     * 物料批次号
     */
    private String batchNumber;
    /**
     * 物料类型Id(对应产品编号)
     */
    private String materialConfigId;
    /**
     * 物料库存Id
     */
    private String materialStockId;
    /**
     * 物料单位
     */
    private String materialUnit;
    /**
     * 采购收货数量
     */
    private BigDecimal signedAmount;
    /**
     * 物料编号
     */
    private String materialNumber;
    /**
     * 物料条码
     */
    private String barCode;
    /**
     * 仓库编码
     */
    private String warehouseCode;
    /**
     * 物料名称
     */
    private String materialName;
    /**
     * 物料属性(1.成品 2.毛坯 3.辅助材料)
     *
     * 枚举 {@link TODO wms_material_type_properties 对应的类}
     */
    private Integer materialProperty;
    /**
     * 物料类型(1.零件 2.托盘 3.工装 4.夹具 5.刀具)
     *
     * 枚举 {@link TODO wms_material_type 对应的类}
     */
    private String materialType;
    /**
     * 物料管理模式
     */
    private Integer materialManage;
    /**
     * 物料规格
     */
    private String materialSpecification;
    /**
     * 物料品牌
     */
    private String materialBrand;
    /**
     * 退货数量
     */
    private BigDecimal consignedAmount;
    /**
     * 库存
     */
    private BigDecimal quantity;

}