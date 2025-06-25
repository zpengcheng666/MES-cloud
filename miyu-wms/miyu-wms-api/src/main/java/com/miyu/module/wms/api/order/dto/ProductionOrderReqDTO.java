package com.miyu.module.wms.api.order.dto;

import com.miyu.module.wms.api.mateiral.dto.MaterialStockRespDTO;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
public class ProductionOrderReqDTO {
    // 是否要料标识
    @NotNull(message = "是否要料标识不能为空")
    private Boolean isNeedMaterial;
    // 目标库位 --- 三坐标专用
//    @NotEmpty(message = "目标库位不能为空")
    // 出库 移库 使用目标位置 如果目标位置非接驳位 则使用目标仓库
    private String targetLocationId;
    // 目标仓库 --- 目标库位所在仓库
//    @NotEmpty(message = "目标仓库不能为空")
    // 入库 使用目标仓库 不存在 赋值物料的默认仓库
    private String targetWarehouseId;
    // 物料条码
    @NotEmpty(message = "物料条码不能为空")
    private String barCode;
    // 物料数量
    @NotNull(message = "物料数量不能为空")
    private Integer quantity;


}
