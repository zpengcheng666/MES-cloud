package com.miyu.module.wms.controller.admin.materialstock.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import javax.validation.constraints.*;

@Schema(description = "管理后台 - 物料库存新增/修改 Request VO")
@Data
public class MaterialStockSaveReqVO {

    @Schema(description = "ID", requiredMode = Schema.RequiredMode.REQUIRED)
    private String id;

    @Schema(description = "物料类型id", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotEmpty(message = "物料类型id不能为空")
    private String materialConfigId;

    @Schema(description = "物料条码")
    private String barCode;

    @Schema(description = "物料批次号")
    private String batchNumber;

    /*@Schema(description = "物料id")
    private String materialId;*/

    @Schema(description = "储位id")
    private String storageId;

    @Schema(description = "库位id")
    private String locationId;

    @Schema(description = "绑定类型")
    private Integer bindType;

    @Schema(description = "总库存", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "总库存不能为空")
    private Integer totality;

    @Schema(description = "锁定库存")
    private Integer locked;

    @Schema(description = "可用库存")
    private Integer available;

    /*@Schema(description = "容器满载比例")
    private Integer byOccupancyRatio;*/

    @Schema(description = "占用比例")
    private Integer occupancyRatio;

    private Boolean isExists;

}