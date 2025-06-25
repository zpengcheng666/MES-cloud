package com.miyu.module.wms.controller.admin.inwarehousedetail.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import javax.validation.constraints.*;

@Schema(description = "管理后台 - 入库详情新增/修改 Request VO")
@Data
public class InWarehouseDetailSaveReqVO {

    @Schema(description = "ID", requiredMode = Schema.RequiredMode.REQUIRED)
    private String id;

    @Schema(description = "入库单号 默认为来源单号；自建单则自动生成", requiredMode = Schema.RequiredMode.REQUIRED)
    private String orderNumber;

    @Schema(description = "入库类型（采购入库、外协入库、生产入库、退料入库、检验入库、其他入库）", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "入库类型（采购入库、外协入库、生产入库、退料入库、检验入库、其他入库）不能为空")
    private Integer inType;


    private String startWarehouseId;

    @Schema(description = "入库仓库ID")
    private String targetWarehouseId;

    @Schema(description = "入库状态（待质检、待入库、待上架、已完成、已关闭）", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "入库状态（待质检、待入库、待上架、已完成、已关闭）不能为空")
    private Integer inState;

    @Schema(description = "物料批次号(冗余方便查询)")
    private String batchNumber;

    @Schema(description = "物料类型id(冗余方便查询)")
    private String materialConfigId;

    @Schema(description = "选择的物料id")
    private String chooseStockId;

    @Schema(description = "实际的物料库存id")
    private String materialStockId;

    @Schema(description = "数量")
    @NotNull(message = "数量不能为空")
    private Integer quantity;

}