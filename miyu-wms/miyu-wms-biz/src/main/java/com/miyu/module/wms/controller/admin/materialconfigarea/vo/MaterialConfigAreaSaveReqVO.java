package com.miyu.module.wms.controller.admin.materialconfigarea.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import javax.validation.constraints.*;

@Schema(description = "管理后台 - 物料类型关联库区配置新增/修改 Request VO")
@Data
public class MaterialConfigAreaSaveReqVO {

    @Schema(description = "ID", requiredMode = Schema.RequiredMode.REQUIRED)
    private String id;

    @Schema(description = "库区编码")
    private String warehouseAreaId;

    @Schema(description = "物料类型")
    private String materialConfigId;

}