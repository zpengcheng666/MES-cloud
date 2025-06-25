package com.miyu.module.pdm.controller.admin.material.vo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema(description = "PDM - 物料列表(包含工装、材料等)-临时 Response VO")
@Data
@ExcelIgnoreUnannotated
public class MaterialRespVO {

    @Schema(description = "物料ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @ExcelProperty("物料ID")
    private String id;

    @Schema(description = "物料编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "111")
    private String materialNumber;

    @Schema(description = "物料名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "111")
    private String materialName;

    @Schema(description = "物料规格", requiredMode = Schema.RequiredMode.REQUIRED, example = "111")
    private String materialSpecification;

    @Schema(description = "物料类码", requiredMode = Schema.RequiredMode.REQUIRED, example = "111")
    private String materialCode;
}
