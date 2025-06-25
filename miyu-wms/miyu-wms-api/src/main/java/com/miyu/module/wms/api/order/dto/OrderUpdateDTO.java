package com.miyu.module.wms.api.order.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema(description = "RPC 服务 - 订单")
@Data
public class OrderUpdateDTO {

    /**
     * 订单类型 采购入库 外协入库 生产入库 退料入库 检验入库 其他入库
     * 销售出库 外协出库 生产出库 检验出库 报损出库 采购退货出库 调拨出库 其他出库
     * 生产移库 检验移库 调拨移库
     *
     * 字典已构建 请注意查看  DictConstants-》WMS_ORDER_TYPE_PURCHASE_IN
     * 必填
     */
//    @NotNull(message = "订单类型不能为空")
    private Integer orderType;
    /**
     * 订单状态
     * 非必填  入库单创建使用  默认 1 待入库 ；//0 待质检
     * 字典已构建 请注意查看  DictConstants-》WMS_ORDER_DETAIL_STATUS_0
     */
    private Integer orderStatus;
    /**
     * 目标仓库id
     * 必填
     */
    private String targetWarehouseId;
    /**
     * 物料库存id
     * 必填
     */
    // 真正出库的物料库存id
    private String materialStockId;
}
