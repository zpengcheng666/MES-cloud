package com.miyu.module.wms.api.order.dto;

import com.miyu.module.wms.enums.DictConstants;
import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class SpecifiedTransportationRespDTO {

    // 物料条码
    private String barCode;
    // 起始库位 --- 三坐标专用
    private String startLocationId;
    // 目标库位--- 三坐标专用
    private String targetLocationId;
    // 移库单号
    private String orderNumber;
    // 订单类型 目前仅有 生产移库
    private Integer orderType = DictConstants.WMS_ORDER_TYPE_PRODUCE_MOVE;

}
