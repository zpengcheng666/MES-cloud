package com.miyu.module.pdm.controller.admin.processPlanDetail.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema(description = "管理后台 - PDM 数控程序VersionNc Request VO")
@Data
public class ProcessVersionNcReqVO {
    @Schema(description = "零件工艺规程版本id")
    private String processVersionId;

    @Schema(description = "工序id")
    private String procedureId;

    @Schema(description = "工步id")
    private String stepId;

    @Schema(description = "数控程序id")
    private String ncId;
}
