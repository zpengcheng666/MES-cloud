package com.miyu.module.wms.controller.admin.movewarehousedetail.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Schema(description = "管理后台 - 库存移动详情新增/修改 Request VO")
@Data
public class MoveWarehouseDetailSaveReqVO {

    @Schema(description = "ID", requiredMode = Schema.RequiredMode.REQUIRED)
    private String id;

    @Schema(description = "移库单号")
    private String orderNumber;

    @Schema(description = "移库类型（生产移库，检验移库，调拨移库）")
    private Integer moveType;

    @Schema(description = "移库状态（待移交、待送达、待签收、已完成、已关闭）")
    private Integer moveState;

    @Schema(description = "起始仓库id")
    private String startWarehouseId;

    @Schema(description = "目标仓库id")
    private String targetWarehouseId;

    @Schema(description = "物料批次号(冗余方便查询)")
    private String batchNumber;

    @Schema(description = "物料类型id(冗余方便查询)")
    private String materialConfigId;

    @Schema(description = "选择的物料库存id")
    private String chooseStockId;

    @Schema(description = "实际的物料库存id")
    private String materialStockId;

    @Schema(description = "数量")
    private Integer quantity;

}