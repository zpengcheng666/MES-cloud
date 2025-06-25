package com.miyu.module.qms.controller.admin.inspectionsheet.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.ToString;
import javax.validation.constraints.NotEmpty;

@Schema(description = "管理后台 - 检验单 Request VO")
@Data
@ToString(callSuper = true)
public class InspectionSheetReqVO {

    @Schema(description = "检验单任务ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "123")
    @NotEmpty(message = "检验任单务ID不能为空")
    private String sheetSchemeId;

    @Schema(description = "检测单产品ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "123")
    @NotEmpty(message = "检验单产品ID不能为空")
    private String sheetSchemeMaterialId;

}