package com.miyu.module.ppm.api.shipping.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema(description = "RPC 服务 - 销售系统  出库完成请求")
@Data
public class ShippingOutboundReqDTO {

    /***
     * 发货单编号
     */
    private String  shippingNo;

    /***
     * 出库状态  1 完成 2 作废
     */
    private Integer status;
}
