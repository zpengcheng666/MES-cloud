package com.miyu.module.wms.controller.admin.outwarehousedetail.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import javax.validation.constraints.*;

@Schema(description = "管理后台 - 出库详情新增/修改 Request VO")
@Data
public class OutWarehouseDetailSaveReqVO {

    @Schema(description = "ID", requiredMode = Schema.RequiredMode.REQUIRED)
    private String id;

    @Schema(description = "出库单号", requiredMode = Schema.RequiredMode.REQUIRED)
    private String orderNumber;

    @Schema(description = "出库类型（销售出库、外协出库、生产出库、检验出库、报损出库、采购退货出库、调拨出库、其他出库）")
    private Integer outType;

    @Schema(description = "出库状态（待出库、待送达、待签收、已完成、已关闭）", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "出库状态（待出库、待送达、待签收、已完成、已关闭）不能为空")
    private Integer outState;

    @Schema(description = "出库仓库id")
    private String startWarehouseId;

    @Schema(description = "目标仓库id")
    private String targetWarehouseId;

    @Schema(description = "物料批次号(冗余方便查询)")
    private String batchNumber;

    @Schema(description = "物料类型id(冗余方便查询)")
    private String materialConfigId;

    @Schema(description = "选择的物料库存id", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotEmpty(message = "选择的物料库存id不能为空")
    private String chooseStockId;

    @Schema(description = "实际的物料库存id")
    private String materialStockId;

    @Schema(description = "数量", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "数量不能为空")
    private Integer quantity;

}