package com.miyu.module.ppm.api.purchaseConsignment.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Schema(description = "RPC 服务 - 采购系统  采购收货")
@Data
public class ConsignmentSignDTO {

    /***
     * 收货单信息ID  必填
     */
    private String  consignmentInfoId;


    /***
     * 数量（签收数量用）
     */
    private Integer number;
    /***
     * 数量（签收条码用）
     */
    private List<String> barCodes;


    private String locationId;

}
