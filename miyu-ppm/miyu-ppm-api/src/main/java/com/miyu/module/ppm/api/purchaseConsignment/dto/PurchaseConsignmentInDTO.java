package com.miyu.module.ppm.api.purchaseConsignment.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema(description = "RPC 服务 - 采购系统  入库完成请求")
@Data
public class PurchaseConsignmentInDTO {

    /***
     * 收货单Id
     */
    private String ConsignmentId;

    /***
     * 入库状态  1 完成 2 作废
     */
    private Integer status;

}
