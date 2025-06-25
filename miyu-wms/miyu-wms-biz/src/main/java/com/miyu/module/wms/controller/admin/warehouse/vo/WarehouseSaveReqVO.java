package com.miyu.module.wms.controller.admin.warehouse.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import javax.validation.constraints.*;

@Schema(description = "管理后台 - 仓库表新增/修改 Request VO")
@Data
public class WarehouseSaveReqVO {

    @Schema(description = "ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "11444")
    private String id;

    @Schema(description = "仓库名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "王五")
    @NotEmpty(message = "仓库名称不能为空")
    private String warehouseName;

    @Schema(description = "仓库编码", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotEmpty(message = "仓库编码不能为空")
    private String warehouseCode;

    @Schema(description = "仓库地址")
    private String warehouseAddress;

    @Schema(description = "仓库容量")
    private String warehouseCapacity;

    @Schema(description = "仓库性质", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "仓库性质不能为空")
    private Integer warehouseNature;

    @Schema(description = "仓库类型", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @NotNull(message = "仓库类型不能为空")
    private Integer warehouseType;

    @Schema(description = "仓库状态", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "仓库状态不能为空")
    private Integer warehouseState;

    @Schema(description = "仓库主管", example = "21840")
    private String userId;

}