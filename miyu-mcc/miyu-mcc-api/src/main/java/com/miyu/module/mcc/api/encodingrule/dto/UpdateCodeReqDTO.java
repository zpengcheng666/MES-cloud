package com.miyu.module.mcc.api.encodingrule.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema(description = "RPC 服务 - 销售系统  发货单明细")
@Data
public class UpdateCodeReqDTO {

    @Schema(description = "编码")
    private String code;
}
