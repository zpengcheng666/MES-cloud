package com.miyu.module.ppm.api.purchaseConsignment.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Schema(description = "RPC 服务 - 采购系统  入库完成接收")
@Data
public class WmsDTO {

    /***
     * 收货单Id
     */
    private String ConsignmentId;

    /***
     * 收货单详细
     */
    private List<WmsMaterialInfo> infoList;

    @Data
    public static class WmsMaterialInfo {
        /***
         * 产品
         */
        private String materialTypeId;
        /***
         * 数量
         */
        private BigDecimal number;
    }

}
