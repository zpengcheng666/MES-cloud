package com.miyu.module.pdm.controller.admin.feasibilityDetail.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema(description = "管理后台 - PDM 选中资源 Request VO")
@Data
public class FeasibilityDetailReqVO {

    @Schema(description = "项目编号")
    private String projectCode;

    @Schema(description = "零部件版本ID")
    private String partVersionId;

    @Schema(description = "资源类型")
    private Integer resourcesType;

    @Schema(description = "资源id")
    private String resourcesTypeId;

    @Schema(description = "预估工时(min)")
    private Integer processingTime;
}
