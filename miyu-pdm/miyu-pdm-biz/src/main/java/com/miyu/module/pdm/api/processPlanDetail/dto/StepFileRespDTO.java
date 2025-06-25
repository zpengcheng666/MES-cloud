package com.miyu.module.pdm.api.processPlanDetail.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema(description = "RPC 服务 - 工步草图信息")
@Data
public class StepFileRespDTO {

    @Schema(description = "草图编号")
    private String sketchCode;

    @Schema(description = "草图地址")
    private String sketchUrl;

}
