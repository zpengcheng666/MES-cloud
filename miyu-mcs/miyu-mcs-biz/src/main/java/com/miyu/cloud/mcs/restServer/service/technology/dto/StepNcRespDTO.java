package com.miyu.cloud.mcs.restServer.service.technology.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema(description = "RPC 服务 - 工步程序信息")
@Data
public class StepNcRespDTO {

    @Schema(description = "程序名称")
    private String ncName;

    @Schema(description = "程序地址")
    private String ncUrl;

}
