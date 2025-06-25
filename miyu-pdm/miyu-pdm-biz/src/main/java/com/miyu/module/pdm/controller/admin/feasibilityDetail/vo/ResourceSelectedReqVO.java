package com.miyu.module.pdm.controller.admin.feasibilityDetail.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Schema(description = "管理后台 - PDM 选中资源(设备、刀具、工装) Request VO")
@Data
public class ResourceSelectedReqVO {

    private List<String> ids;

    @Schema(description = "项目编号")
    private String projectCode;

    @Schema(description = "零部件版本ID")
    private String partVersionId;

    @Schema(description = "零件工艺规程版本id")
    private String processVersionId;

    @Schema(description = "资源类型")
    private Integer resourcesType;

    @Schema(description = "工序id")
    private String procedureId;

    @Schema(description = "工步id")
    private String stepId;
}
