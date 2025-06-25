package com.miyu.module.pdm.api.processPlan.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema(description = "零件信息 Response VO")
@Data
public class PartRespDTO {

    @Schema(description = "PMID", example = "111")
    private String id;

    @Schema(description = "零件图号", example = "test")
    private String partNumber;

    @Schema(description = "零件名称", example = "test")
    private String partName;

    @Schema(description = "加工状态", example = "A状态")
    private String processCondition;

}
