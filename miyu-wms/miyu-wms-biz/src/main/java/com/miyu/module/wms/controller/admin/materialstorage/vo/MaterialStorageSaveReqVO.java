package com.miyu.module.wms.controller.admin.materialstorage.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import javax.validation.constraints.*;

@Schema(description = "管理后台 - 物料储位新增/修改 Request VO")
@Data
public class MaterialStorageSaveReqVO {

    @Schema(description = "ID", requiredMode = Schema.RequiredMode.REQUIRED)
    private String id;

    @Schema(description = "储位编码", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotEmpty(message = "储位编码不能为空")
    private String storageCode;

    @Schema(description = "储位名称")
    private String storageName;

    @Schema(description = "物料id", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotEmpty(message = "物料id不能为空")
    private String materialStockId;

    @Schema(description = "是否有效")
    private String valid;

}