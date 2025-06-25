package com.miyu.module.wms.api.order.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Schema(description = "RPC 服务 - 订单")
@Data
public class OrderReqDTO {

    /**
     * 移库单号
     * 必填
     */
//    @NotBlank(message = "移库单号不能为空")
    private String orderNumber;
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
     * 起始仓库id
     * 入库不填   出库、移库可选
     */
    private String startWarehouseId;
    /**
     * 目标仓库id
     * 入库选填（默认为承载托盘的默认仓库）   出库、移库必填
     */
    private String targetWarehouseId;
    /**
     * 物料批次号
     */
    private String batchNumber;
    /**
     * 物料类型id
     * 非必填 采购入库专用
     */
    private String materialConfigId;
    /**
     * 物料库存id
     */
    // 选择的物料库存id 必填
    private String chooseStockId;
    /**
     * 物料库存id
     */
    // 真正出库的物料库存id 返回值
    private String materialStockId;
    private String realBarCode;
    /**
     * 数量
     */
//    @NotNull(message = "数量不能为空")
    // 数量 必填
    private Integer quantity;
    /**
     * 签收库位id
     * 非必填 操作人创建出库移库单时 传入其签收库位id 用于根据签收库位查询出库单状态
     */
    private String signLocationId;

    /**
     * 操作人
     * 仅查询使用
     */
    private String operator;
    /**
     * 操作时间
     * 仅查询使用
     */
    private LocalDateTime operateTime;
    /**
     * 签收人
     * 仅查询使用
     */
    private String signer;
    /**
     * 签收时间
     * 仅查询使用
     */
    private LocalDateTime signTime;
    /**
     * 需求时间-- 出库专用  按需求时间排序，为空时默认当前时间
     */
    private LocalDateTime needTime;
}
