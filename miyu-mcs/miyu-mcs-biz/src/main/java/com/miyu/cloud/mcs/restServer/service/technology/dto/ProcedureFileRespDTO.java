package com.miyu.cloud.mcs.restServer.service.technology.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema(description = "RPC 服务 - 工序草图信息")
@Data
public class ProcedureFileRespDTO {

    @Schema(description = "草图编号")
    private String sketchCode;

    @Schema(description = "草图地址")
    private String sketchUrl;

}
