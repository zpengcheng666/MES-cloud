package com.miyu.module.wms.api.order.dto;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
public class SpecifiedTransportationReqDTO {

    // 物料条码
    @NotEmpty(message = "物料条码不能为空")
    private String barCode;
    // 起始库位 --- 三坐标专用
    @NotEmpty(message = "起始库位不能为空")
    private String startLocationId;
    // 目标库位--- 三坐标专用
    @NotEmpty(message = "目标库位不能为空")
    private String targetLocationId;
}
