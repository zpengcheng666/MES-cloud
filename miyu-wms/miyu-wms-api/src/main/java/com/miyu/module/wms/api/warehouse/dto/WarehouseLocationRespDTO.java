package com.miyu.module.wms.api.warehouse.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import java.time.LocalDateTime;

@Schema(description = "RPC 服务 - 库位")
@Data
public class WarehouseLocationRespDTO {

    @Schema(description = "ID", requiredMode = Schema.RequiredMode.REQUIRED)
    private String id;

    @Schema(description = "创建时间", requiredMode = Schema.RequiredMode.REQUIRED)
    private LocalDateTime createTime;

    @Schema(description = "库位编码")
    private String locationCode;

    @Schema(description = "库位名称")
    private String locationName;

    @Schema(description = "库区id")
    private String warehouseAreaId;

    @Schema(description = "库区编码")
    private String areaCode;

    @Schema(description = "是否锁定")
    private Integer locked;

    @Schema(description = "是否有效")
    private Integer valid;

    @Schema(description = "仓库编码")
    private String warehouseCode;

    @Schema(description = "库区名称")
    private String areaName;

    @Schema(description = "库区属性")
    private Integer areaProperty;

    @Schema(description = "库区类型")
    private Integer areaType;

    @Schema(description = "位（前台展示排序用）")
    private Integer site;

}
