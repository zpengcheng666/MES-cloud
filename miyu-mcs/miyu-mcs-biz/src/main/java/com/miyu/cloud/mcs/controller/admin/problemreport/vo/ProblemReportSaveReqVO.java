package com.miyu.cloud.mcs.controller.admin.problemreport.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema(description = "管理后台 - 问题上报新增/修改 Request VO")
@Data
public class ProblemReportSaveReqVO {

    @Schema(description = "id", requiredMode = Schema.RequiredMode.REQUIRED, example = "3993")
    private String id;

    @Schema(description = "工位id")
    private String stationId;

    @Schema(description = "问题类型", example = "1")
    private Integer type;

    @Schema(description = "上报id", example = "14910")
    private String reportId;

    @Schema(description = "状态", example = "1")
    private String status;

    @Schema(description = "问题描述")
    private String content;

}