package com.miyu.cloud.mcs.dto.externalManufacture;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Data
public class McsMeasureResultDataDTO {
    //零件号
    @Schema(description = "零件号")
    private String partID;
    //测量项数量
    @Schema(description = "测量项数量")
    private String amount;
    //超差数量
    @Schema(description = "超差数量")
    private String outTolNum;
    //点信息
    @Schema(description = "点信息")
    private List<McsMeasureResultPointDataDTO> items;

}
