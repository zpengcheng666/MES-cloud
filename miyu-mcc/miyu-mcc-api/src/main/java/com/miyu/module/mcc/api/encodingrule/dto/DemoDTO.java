package com.miyu.module.mcc.api.encodingrule.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema(description = "RPC 服务 - 销售系统  发货单明细")
@Data
public class DemoDTO {

    @Schema(description = "类型编码(图号-工序)(半成品编号)")
    private String materialConfigNumber;

    @Schema(description = "零件图号(成品编号)")
    private String partNumber;


    @Schema(description = "类型id(半成品类型id)")
    private String materialConfigId;

    @Schema(description = "零件类型id(成品类型id)")
    private String partTypeId;
}
