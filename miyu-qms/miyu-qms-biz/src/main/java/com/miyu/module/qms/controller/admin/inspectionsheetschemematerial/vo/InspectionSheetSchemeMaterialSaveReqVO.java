package com.miyu.module.qms.controller.admin.inspectionsheetschemematerial.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Schema(description = "管理后台 - 检验单产品新增/修改 Request VO")
@Data
public class InspectionSheetSchemeMaterialSaveReqVO {

    @Schema(description = "主键", requiredMode = Schema.RequiredMode.REQUIRED, example = "28969")
    private String id;

    @Schema(description = "测量结果")
    private String content;

    @Schema(description = "是否合格")
    private Integer inspectionResult;

    @Schema(description = "物料ID", example = "3274")
    private String materialId;

    @Schema(description = "物料类型ID", example = "12392")
    private String materialConfigId;

    @Schema(description = "物料条码")
    private String barCode;

    @Schema(description = "物料批次号")
    private String batchNumber;

}