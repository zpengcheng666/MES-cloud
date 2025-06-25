package com.miyu.module.pdm.controller.admin.feasibilityDetail.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema(description = "管理后台 - PDM 量具采购意见 Request VO")
@Data
public class DemandMeasureReqVO {

    @Schema(description = "id", requiredMode = Schema.RequiredMode.REQUIRED, example = "6902")
    private String id;

    @Schema(description = "项目编号")
    private String projectCode;

    @Schema(description = "零部件版本ID")
    private String partVersionId;

    @Schema(description = "量具编号")
    private String measureCode;

    @Schema(description = "量具名称")
    private String measureName;

    @Schema(description = "量具规格")
    private String measureSpecification;

    @Schema(description = "备注")
    private String description;
}
