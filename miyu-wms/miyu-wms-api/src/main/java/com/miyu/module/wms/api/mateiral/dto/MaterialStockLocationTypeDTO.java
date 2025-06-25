package com.miyu.module.wms.api.mateiral.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

@Schema(description = "RPC 服务 - 获取物料库存")
@Data
public class MaterialStockLocationTypeDTO {

    @Schema(description = "ID", requiredMode = Schema.RequiredMode.REQUIRED)
    private String id;

    @Schema(description = "创建时间", requiredMode = Schema.RequiredMode.REQUIRED)
    private LocalDateTime createTime;

    @Schema(description = "物料类型id", requiredMode = Schema.RequiredMode.REQUIRED)
    private String materialConfigId;

    @Schema(description = "物料管理模式，1：单件，2：批量", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer materialManage;

    @Schema(description = "物料编号", requiredMode = Schema.RequiredMode.REQUIRED)
    private String materialNumber;

    @Schema(description = "物料条码", requiredMode = Schema.RequiredMode.REQUIRED)
    private String barCode;

    @Schema(description = "物料批次号", requiredMode = Schema.RequiredMode.REQUIRED)
    private String batchNumber;

    @Schema(description = "储位id", requiredMode = Schema.RequiredMode.REQUIRED)
    private String storageId;

    @Schema(description = "库位id", requiredMode = Schema.RequiredMode.REQUIRED)
    private String locationId;

    @Schema(description = "总库存")
    private Integer totality;
    /**
     * 库区类型
     * 枚举 DictConstants.WMS_WAREHOUSE_AREA_TYPE_1
     */
    private Integer areaType;
    /**
     * 库区属性
     * 枚举 DictConstants.WMS_WAREHOUSE_AREA_PROPERTY_NOT_AUTO
     */
    private Integer areaProperty;
    /**
     * 仓库类型
     * 枚举 DictConstants.WMS_WAREHOUSE_TYPE_1
     */
    private Integer warehouseType;

    /**
     * 仓库id
     */
    private String warehouseId;
}
