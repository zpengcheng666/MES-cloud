package com.miyu.module.ppm.api.shipping.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Schema(description = "RPC 服务 - 销售系统  销售发货")
@Data
public class ShippingOutDTO {

    /***
     * 发货单信息ID  必填
     */
    private String  shippingInfoId;

    /***
     * 条码集合  签收条码必填 （生成出库单不填）
     */
    private List<String> barCodes;

    /***
     * 位置信息
     */
    private String locationId;
}
