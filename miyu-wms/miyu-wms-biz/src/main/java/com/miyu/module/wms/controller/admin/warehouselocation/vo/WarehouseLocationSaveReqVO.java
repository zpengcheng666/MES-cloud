package com.miyu.module.wms.controller.admin.warehouselocation.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import javax.validation.constraints.*;

@Schema(description = "管理后台 - 库位新增/修改 Request VO")
@Data
public class WarehouseLocationSaveReqVO {

    @Schema(description = "ID", requiredMode = Schema.RequiredMode.REQUIRED)
    private String id;

    @Schema(description = "库位编码")
    private String locationCode;

    @Schema(description = "库位名称")
    private String locationName;

    @Schema(description = "库区id")
    private String warehouseAreaId;

    @Schema(description = "是否锁定")
    private Integer locked;

    @Schema(description = "是否有效")
    private Integer valid;

}