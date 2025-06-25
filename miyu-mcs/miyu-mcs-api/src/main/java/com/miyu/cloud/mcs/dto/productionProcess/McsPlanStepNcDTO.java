package com.miyu.cloud.mcs.dto.productionProcess;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 加工程序
 */
@Schema(description = "RPC 服务 - 工步程序信息")
@Data
public class McsPlanStepNcDTO {

    @Schema(description = "程序名称")
    private String ncName;

    @Schema(description = "程序地址")
    private String ncUrl;

}
