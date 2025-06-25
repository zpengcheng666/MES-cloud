package com.miyu.module.pdm.controller.admin.dataImport.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema(description = "管理后台 - PDM 数据包导入 Request VO")
@Data
public class DataImportReqVO {

    @Schema(description = "产品id", requiredMode = Schema.RequiredMode.REQUIRED, example = "111")
    private String rootProductId;

    @Schema(description = "项目编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "111")
    private String projectCode;

    @Schema(description = "厂家id", requiredMode = Schema.RequiredMode.REQUIRED, example = "111")
    private String companyId;

    @Schema(description = "厂家名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "111")
    private String companyName;

    @Schema(description = "结构编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "5860")
    private Long structureId;
}
