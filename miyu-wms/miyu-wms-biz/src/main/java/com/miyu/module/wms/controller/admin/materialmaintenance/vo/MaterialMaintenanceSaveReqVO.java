package com.miyu.module.wms.controller.admin.materialmaintenance.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import javax.validation.constraints.*;

@Schema(description = "管理后台 - 物料维护记录新增/修改 Request VO")
@Data
public class MaterialMaintenanceSaveReqVO {

    @Schema(description = "ID", requiredMode = Schema.RequiredMode.REQUIRED)
    private String id;

    @Schema(description = "物料库存id")
    private String materialStockId;

    @Schema(description = "描述", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotEmpty(message = "描述不能为空")
    private String description;

    @Schema(description = "数量")
    private Integer quantity;

    @Schema(description = "维护类型(1报废，2装配，3加工)")
    private Integer type;

}