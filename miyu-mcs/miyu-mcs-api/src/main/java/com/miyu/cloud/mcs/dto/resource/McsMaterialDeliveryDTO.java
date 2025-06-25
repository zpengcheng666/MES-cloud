package com.miyu.cloud.mcs.dto.resource;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class McsMaterialDeliveryDTO {

    //物料编码
    @Schema(description = "物料编码", example = "BC-1234")
    private String materialCode;
    //配送状态
    @Schema(description = "配送状态", example = "1:物料配送中;2:待签收;3:载具配送中;4:待配送")
    private Integer status;
}
