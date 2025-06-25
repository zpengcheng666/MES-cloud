package com.miyu.module.qms.controller.admin.inspectionsheetscheme.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotEmpty;


@Schema(description = "管理后台 - 生产终端任务检验 Request VO")
@Data
public class InspectionSchemeTerminalValidReqVO {
    @Schema(description = "物料条码", requiredMode = Schema.RequiredMode.REQUIRED, example = "1234")
    @NotEmpty(message = "物料条码不能为空")
    private String barCode;

    @Schema(description = "检验任务ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "1234")
    @NotEmpty(message = "检验任务不能为空")
    private String schemeId;
}
