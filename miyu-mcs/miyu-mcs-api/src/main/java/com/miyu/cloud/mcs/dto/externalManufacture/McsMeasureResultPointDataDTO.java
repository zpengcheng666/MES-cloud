package com.miyu.cloud.mcs.dto.externalManufacture;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class McsMeasureResultPointDataDTO {
    //位置
    @Schema(description = "位置")
    private String dimension;
    //名称
    @Schema(description = "名称")
    private String name;
    //实测值
    @Schema(description = "实测值")
    private String actual;
    //理论值
    @Schema(description = "理论值")
    private String nominal;
    //上公差
    @Schema(description = "上公差")
    private String uTol;
    //下公差
    @Schema(description = "下公差")
    private String lTol;
    //偏差
    @Schema(description = "偏差")
    private String dev;
    //超差值
    @Schema(description = "超差值")
    private String outTol;
    //是否合格，1-合格,2-不合格
    @Schema(description = "是否合格", example = "1-合格,2-不合格")
    private String result;

}
