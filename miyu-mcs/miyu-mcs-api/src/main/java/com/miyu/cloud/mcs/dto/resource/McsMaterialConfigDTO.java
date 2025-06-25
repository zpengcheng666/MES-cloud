package com.miyu.cloud.mcs.dto.resource;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 物料类型
 */
@Data
public class McsMaterialConfigDTO {

    @Schema(description = "类型编码(图号-工序)(半成品编号)")
    private String materialConfigNumber;

    @Schema(description = "零件图号(成品编号)")
    private String partNumber;


    @Schema(description = "类型id(半成品类型id)")
    private String materialConfigId;

    @Schema(description = "零件类型id(成品类型id)")
    private String partTypeId;
}
