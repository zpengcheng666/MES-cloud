package com.miyu.module.wms.api.order.dto;

import com.miyu.module.wms.api.mateiral.dto.MaterialStockRespDTO;
import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class ProductionOrderRespDTO {
    // 是否要料标识
    private Boolean isNeedMaterial;
    // 目标库位
    private String targetLocationId;
    // 目标仓库
    private String targetWarehouseId;
    // 物料条码
    private String barCode;
    // 物料数量
    private Integer quantity;
    // 出入库单号
    private String orderNumber;
    // 订单类型 生产入库、生产出库、生产移库
    private Integer orderType;



    //**************************************分界线*********************************************//
    // 物料所在仓库
    private String atWarehouseId;
    // 物料实体
    private MaterialStockRespDTO materialStock;
}
