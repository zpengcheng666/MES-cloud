package com.miyu.module.wms.controller.admin.operatingterminal.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
public class OutWarehouseDetailOperationVO {

    @Schema(description = "ID", requiredMode = Schema.RequiredMode.REQUIRED)
    private String id;

    @Schema(description = "出库单号", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotEmpty(message = "出库单号不能为空")
    private String orderNumber;

    @Schema(description = "物料批次号(冗余方便查询)", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotEmpty(message = "物料批次号(冗余方便查询)不能为空")
    private String batchNumber;

    @Schema(description = "物料类型id(冗余方便查询)", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotEmpty(message = "物料类型id(冗余方便查询)不能为空")
    private String materialConfigId;
    private String materialNumber;

    @Schema(description = "物料库存id", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotEmpty(message = "物料库存id不能为空")
    private String materialStockId;
    private String barCode;


    @Schema(description = "绑定位置id", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotEmpty(message = "绑定位置id不能为空")
    private String bindPositionId;
    private String bindPositionCode;

    @Schema(description = "绑定类型", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotEmpty(message = "绑定类型不能为空")
    private String bindType;

    @Schema(description = "数量", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "数量不能为空")
    private Integer quantity;

}
