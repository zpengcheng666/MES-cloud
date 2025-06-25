package com.miyu.module.qms.controller.admin.inspectionsheetsamplingrule.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Schema(description = "管理后台 - 检验单抽样规则（检验抽样方案）关系新增/修改 Request VO")
@Data
public class InspectionSheetSamplingRuleSaveReqVO {

    @Schema(description = "主键", requiredMode = Schema.RequiredMode.REQUIRED, example = "485")
    private String id;

    @Schema(description = "检测任务ID", example = "32464")
    private String inspectionSheetSchemeId;

    @Schema(description = "检测项目ID", example = "21674")
    private String inspectionSchemeItemId;

    @Schema(description = "抽样方案ID", example = "23434")
    private String samplingRuleConfigId;

    @Schema(description = "抽样标准ID", example = "1128")
    private String samplingStandardId;

}