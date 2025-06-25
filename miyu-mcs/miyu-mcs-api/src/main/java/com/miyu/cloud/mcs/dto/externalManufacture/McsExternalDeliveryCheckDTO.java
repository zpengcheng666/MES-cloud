package com.miyu.cloud.mcs.dto.externalManufacture;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class McsExternalDeliveryCheckDTO {

    //设备编码
    @Schema(description = "开始时间", requiredMode = Schema.RequiredMode.REQUIRED, example = "1735893547000")
    private String deviceCode;
    //物料编码
    @Schema(description = "物料编码", requiredMode = Schema.RequiredMode.REQUIRED, example = "BC-1234")
    private String materialCode;
    //操作人
    @Schema(description = "操作人", requiredMode = Schema.RequiredMode.REQUIRED, example = "123456")
    private String operator;
}
