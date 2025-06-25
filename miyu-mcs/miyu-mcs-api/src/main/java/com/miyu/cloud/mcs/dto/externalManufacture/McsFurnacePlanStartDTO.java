package com.miyu.cloud.mcs.dto.externalManufacture;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Data
public class McsFurnacePlanStartDTO {

    //物料编码集合
    @Schema(description = "物料编码集合", requiredMode = Schema.RequiredMode.REQUIRED, example = "['BC-1234','BC-2341'")
    private List<String> materialCodeList;
    //设备编码
    @Schema(description = "设备编码", requiredMode = Schema.RequiredMode.REQUIRED, example = "abc")
    private String deviceNumber;
}
