package com.miyu.module.pdm.controller.admin.feasibilityDetail.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema(description = "管理后台 - PDM 刀具采购意见 Request VO")
@Data
public class DemandHiltReqVO {

    @Schema(description = "采购意见id", example = "1")
    private String id;

    @Schema(description = "项目编号")
    private String projectCode;

    @Schema(description = "零部件版本ID")
    private String partVersionId;

    @Schema(description = "接口型式id")
    private String taperTypeId;

    @Schema(description = "刀柄长度")
    private String length;

    @Schema(description = "刀柄前端直径")
    private String frontEndDiameter;

    @Schema(description = "刀柄前端长度")
    private String frontEndLength;

    @Schema(description = "倾角")
    private String dipAngle;

    @Schema(description = "备注")
    private String description;
}
