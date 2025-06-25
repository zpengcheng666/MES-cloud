package com.miyu.module.pdm.api.processPlanDetail.dto;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema(description = "rpc - 工艺规程编制 Response DTO")
@Data
public class ProcedureRespDTO {
    @Schema(description = "id", requiredMode = Schema.RequiredMode.REQUIRED, example = "6902")
    private String id;

    @Schema(description = "零件工艺规程版本id")
    private String processVersionId;

    @Schema(description = "工序序号")
    private String procedureNum;

    @Schema(description = "工序名称(加工路线)")
    private String procedureName;

    @Schema(description = "是否检验")
    private String isInspect;

    @Schema(description = "关重属性")
    private Integer procedureProperty;

    @Schema(description = "准备工时")
    private Integer preparationTime;

    @Schema(description = "加工工时")
    private Integer processingTime;

    @Schema(description = "工作说明")
    private String description;

    @Schema(description = "工作说明预览")
    private String descriptionPreview;

    @Schema(description = "是否外委")
    private String isOut;

    //工艺信息

    @Schema(description = "工艺规程编号", example = "123")
    private String processCode;

    @Schema(description = "工艺规程名称")
    private String processName;
}
