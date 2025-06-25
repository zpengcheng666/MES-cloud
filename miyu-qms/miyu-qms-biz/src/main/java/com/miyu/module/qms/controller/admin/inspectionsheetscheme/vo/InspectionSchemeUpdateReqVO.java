package com.miyu.module.qms.controller.admin.inspectionsheetscheme.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;


@Schema(description = "管理后台 - 任务检验 Request VO")
@Data
public class InspectionSchemeUpdateReqVO {

    @Schema(description = "检测单ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "1234")
    @NotEmpty(message = "检测单ID不能为空")
    private String sheetId;

    @Schema(description = "检测任务ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "1234")
    @NotEmpty(message = "检测任务ID不能为空")
    private String sheetSchemeId;

    @Schema(description = "产品检验结果", requiredMode = Schema.RequiredMode.REQUIRED, example = "1234")
    @NotNull(message = "产品检验结果不能为空")
    private Integer inspectionResult;

    @Schema(description = "通过准则", requiredMode = Schema.RequiredMode.REQUIRED, example = "1234")
    @NotEmpty(message = "通过准则不能为空")
    private String passRule;

    @Schema(description = "合格数量", requiredMode = Schema.RequiredMode.REQUIRED, example = "1234")
    @NotNull(message = "合格数量不能为空")
    private Integer qualifiedQuantity;
}
