package com.miyu.module.wms.api.mateiral.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema(description = "RPC 服务 - 获取仓库")
@Data
public class WarehouseRespDTO {

    @Schema(description = "ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "11444")
    private String id;

    @Schema(description = "仓库名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "王五")
    private String warehouseName;

    @Schema(description = "仓库编码", requiredMode = Schema.RequiredMode.REQUIRED)
    private String warehouseCode;

    @Schema(description = "仓库地址")
    private String warehouseAddress;

    @Schema(description = "仓库容量")
    private String warehouseCapacity;

    @Schema(description = "仓库性质", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer warehouseNature;

    @Schema(description = "仓库类型", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    private Integer warehouseType;

    @Schema(description = "仓库状态", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer warehouseState;

    @Schema(description = "仓库主管ID", example = "21840")
    private String userId;

    @Schema(description = "仓库主管名称", example = "启明")
    private String nickName;

}
