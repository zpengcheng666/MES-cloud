package com.miyu.module.ppm.api.shipping.dto;


import com.miyu.module.ppm.enums.shipping.ShippingStatusEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Schema(description = "RPC 服务 - 销售系统  出库完成请求")
@Data
public class WmsOutBoundInfoDTO {

    /***
     * 出库单Id
     */
    private String shippingId;

    /***
     * 发货单名称
     */
    private String shippingName;

    /***
     * 出库单编码
     */
    private String shippingNo;


    /**
     * 状态  0已创建 1审批中 2 待出库 3待发货4运输中 5结束 9取消
     *
     * 枚举 {@link ShippingStatusEnum}
     */
    private Integer shippingStatus;

    /***
     * 发货单详细
     */
    private List<WmsMaterialInfo> infoList;

    @Data
    public static class WmsMaterialInfo {
        /**
         * 物料库存ID
         */
        private String materialStockId;
        /**
         * 物料类型
         */
        private String materialTypeId;
        /**
         * 物料条码
         */
        private String barCode;
        /**
         * 物料批次号
         */
        private String batchNumber;
        /**
         * 发货数量
         */
        private BigDecimal consignedAmount;
    }

}
