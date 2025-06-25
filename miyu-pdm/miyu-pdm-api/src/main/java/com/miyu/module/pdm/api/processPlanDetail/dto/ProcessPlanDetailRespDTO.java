package com.miyu.module.pdm.api.processPlanDetail.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "工艺计划明细")
public class ProcessPlanDetailRespDTO {

    @Schema(description = "工艺规程版本id")
    private String processVersionId;

    @Schema(description = "工艺规程编号", example = "123")
    private String processCode;

    @Schema(description = "加工方案码", example = "123")
    private String processSchemeCode;

    @Schema(description = "工艺规程名称")
    private String processName;

    @Schema(description = "版次")
    private String processVersion;

    @Schema(description = "零件图号")
    private String partNumber;

    @Schema(description = "零件名")
    private String partName;

    @Schema(description = "零件版本")
    private String partVersion;

}
