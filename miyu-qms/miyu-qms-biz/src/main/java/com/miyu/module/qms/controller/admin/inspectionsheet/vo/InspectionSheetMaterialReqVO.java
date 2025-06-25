package com.miyu.module.qms.controller.admin.inspectionsheet.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.ToString;

import javax.validation.constraints.NotEmpty;

@Schema(description = "管理后台 - 检验单（物料） Request VO")
@Data
@ToString(callSuper = true)
public class InspectionSheetMaterialReqVO {

    @Schema(description = "物料条码", requiredMode = Schema.RequiredMode.REQUIRED, example = "123")
    @NotEmpty(message = "物料条码不能为空")
    private String barCode;

    @Schema(description = "批次号", example = "123")
    private String batchNumber;

}
