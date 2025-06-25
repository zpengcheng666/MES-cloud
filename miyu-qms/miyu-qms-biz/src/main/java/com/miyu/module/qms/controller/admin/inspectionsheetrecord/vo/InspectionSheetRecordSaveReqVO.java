package com.miyu.module.qms.controller.admin.inspectionsheetrecord.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Schema(description = "管理后台 - 检验记录新增/修改 Request VO")
@Data
public class InspectionSheetRecordSaveReqVO {

    @Schema(description = "主键", requiredMode = Schema.RequiredMode.REQUIRED, example = "30944")
    private String id;

    @Schema(description = "检测项目ID", example = "21030")
    private String inspectionItemId;

    @Schema(description = "检测项ID", example = "17919")
    private String inspectionItemDetailId;

    @Schema(description = "检验单ID", example = "4647")
    private String inspectionSheetId;

    @Schema(description = "测量结果")
    private String content;

    @Schema(description = "是否合格")
    private Integer inspectionResult;

    @Schema(description = "检验单物料表ID", example = "18307")
    private String schemeMaterialId;

    @Schema(description = "物料条码（如果批量的 根据数量生成多个）")
    private String barCode;

}