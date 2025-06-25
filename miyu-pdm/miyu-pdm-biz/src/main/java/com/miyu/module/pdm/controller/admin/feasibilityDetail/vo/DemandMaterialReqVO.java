package com.miyu.module.pdm.controller.admin.feasibilityDetail.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema(description = "管理后台 - PDM 工装采购意见 Request VO")
@Data
public class DemandMaterialReqVO {

    @Schema(description = "采购意见id", example = "1")
    private String id;

    @Schema(description = "项目编号")
    private String projectCode;

    @Schema(description = "零部件版本ID")
    private String partVersionId;

    @Schema(description = "工装编号")
    private String materialCode;

    @Schema(description = "工装名称")
    private String materialName;

    @Schema(description = "工装规格")
    private String materialSpecification;

    @Schema(description = "备注")
    private String description;
}
