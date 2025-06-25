package com.miyu.module.qms.controller.admin.inspectionsheetschemematerial.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;


@Schema(description = "管理后台 - 产品检验 Request VO")
@Data
public class InspectionMaterialUpdateReqVO {

    @Schema(description = "检测任务ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "1234")
    @NotEmpty(message = "检测任务ID不能为空")
    private String sheetSchemeId;

    @Schema(description = "检验单产品ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "1234")
    @NotEmpty(message = "检验单产品ID不能为空")
    private String sheetSchemeMaterialId;

    @Schema(description = "产品检验结果", requiredMode = Schema.RequiredMode.REQUIRED, example = "1234")
    @NotNull(message = "产品检验结果不能为空")
    private Integer inspectionResult;

    @Schema(description = "测量结果", requiredMode = Schema.RequiredMode.REQUIRED, example = "1234")
    @NotEmpty(message = "测量结果不能为空")
    private String content;
}
