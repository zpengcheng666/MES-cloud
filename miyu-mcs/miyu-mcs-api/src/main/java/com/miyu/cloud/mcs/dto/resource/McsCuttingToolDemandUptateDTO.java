package com.miyu.cloud.mcs.dto.resource;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class McsCuttingToolDemandUptateDTO {

    //配送订单编码
    private String orderNumber;
    //物料id
    private String materialId;
    //物料条码
    private String barCode;

}
