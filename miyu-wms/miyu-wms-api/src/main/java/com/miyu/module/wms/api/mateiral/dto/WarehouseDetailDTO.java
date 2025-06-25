package com.miyu.module.wms.api.materialconfig.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class WarehouseDetailDTO {
    /**
     * ID
     */
    private String id;
    /**
     * 入库单号 默认为来源单号；自建单则自动生成  收货单no/id
     */
    private String orderNumber;
    /**
     * 入库类型（采购入库、外协入库、生产入库、退料入库、检验入库、其他入库）
     *
     * 枚举
     */
    private Integer inType;
    /**
     * 入库仓库ID
     */
    private String warehouseId;
    /**
     * 入库状态（待质检、待入库、待上架、已完成、已关闭）
     *
     * 枚举
     */
    private Integer inState;
    /**
     * 物料批次号(冗余方便查询)
     */
    private String batchNumber;
    /**
     * 物料类型id(冗余方便查询)-----产品编号
     */
    private String materialConfigId;
    /**
     * 物料库存Id
     */
    private String materialStockId;

    /**
     * 物料单位
     */
    private String  materialUnit;

    /**
     * 采购收货数量
     */
    private BigDecimal  signedAmount;

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
     * 物料属性（成品、毛坯、辅助材料）
     */
    private Integer materialProperty;

    /**
     * 物料类型（零件、托盘、工装、夹具、刀具）
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
