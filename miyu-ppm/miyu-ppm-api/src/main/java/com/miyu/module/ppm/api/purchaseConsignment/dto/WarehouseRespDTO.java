package com.miyu.module.ppm.api.purchaseConsignment.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema(description = "WMS服务 - 入库详情表 Response DTO")
@Data
public class WarehouseRespDTO {

    /**
     * ID
     */
    @Schema(description = "主键", requiredMode = Schema.RequiredMode.REQUIRED)
    private String id;

    /**
     * 入库单号(对应收货单号)
     */
    @Schema(description = "入库单号", requiredMode = Schema.RequiredMode.REQUIRED)
    private String orderNumber;

    /**
     * 入库仓库Id
     */
    @Schema(description = "入库仓库Id", requiredMode = Schema.RequiredMode.REQUIRED)
    private String warehouseId;

    /**
     * 入库状态(1.待质检 2.待入库 3.待上架 4.已完成 5.已关闭)
     *
     * 枚举 {@link TODO wms_in_state 对应的类}
     */
    @Schema(description = "入库状态", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer instate;

    /**
     * 物料批次号
     */
    @Schema(description = "物料批次号", requiredMode = Schema.RequiredMode.REQUIRED)
    private String batchNumber;

    /**
     * 物料类型Id(对应产品编号)
     */
    @Schema(description = "物料类型Id", requiredMode = Schema.RequiredMode.REQUIRED)
    private String materialConfigId;

    /**
     * 物料库存Id
     */
    @Schema(description = "物料库存Id", requiredMode = Schema.RequiredMode.REQUIRED)
    private String materialStockId;

    /**
     * 物料单位
     */
    @Schema(description = "物料单位", requiredMode = Schema.RequiredMode.REQUIRED)
    private String meterialUnit;

    /**
     * 采购收货数量
     */
    @Schema(description = "采购收货数量", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long signedAmount;

    /**
     * 物料编号
     */
    @Schema(description = "物料编号", requiredMode = Schema.RequiredMode.REQUIRED)
    private String materialNumber;

    /**
     * 物料条码
     */
    @Schema(description = "物料条码", requiredMode = Schema.RequiredMode.REQUIRED)
    private String barCode;

    /**
     * 仓库编码
     */
    @Schema(description = "仓库编码", requiredMode = Schema.RequiredMode.REQUIRED)
    private String warehouseCode;

    /**
     * 物料名称
     */
    @Schema(description = "物料名称", requiredMode = Schema.RequiredMode.REQUIRED)
    private String materialName;

    /**
     * 物料属性(1.成品 2.毛坯 3.辅助材料)
     * 枚举 {@link TODO wms_material_type_properties 对应的类}
     */
    @Schema(description = "物料属性", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer materialProperty;

    /**
     * 物料类型(1.零件 2.托盘 3.工装 4.夹具 5.刀具)
     * 枚举 {@link TODO wms_material_type 对应的类}
     */
    @Schema(description = "物料类型", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer materialType;

    /**
     * 物料管理模式
     */
    @Schema(description = "物料管理模式", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer materialManage;

    /**
     * 物料规格
     */
    @Schema(description = "物料规格", requiredMode = Schema.RequiredMode.REQUIRED)
    private String materialSpecification;

    /**
     * 物料品牌
     */
    @Schema(description = "物料品牌", requiredMode = Schema.RequiredMode.REQUIRED)
    private String materialBrand;

    /**
     * 退货数量
     */
    @Schema(description = "退货数量", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long consignedAmount;

    /**
     * 库存
     */
    @Schema(description = "库存", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long quantity;

    /**
     * 是否免检  1是 2否
     */
    @Schema(description = "是否免检", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer qualityCheck;
}
