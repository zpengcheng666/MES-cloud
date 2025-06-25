package com.miyu.module.wms.api.mateiral.dto;

import lombok.Data;

@Data
public class ProductToolReqDTO {
    // 成品刀库存id  --分解时使用
    private String materialStockId;
    // 物料类型id  -- 生成时使用
    private String materialConfigId;
    // 物料储位  -- 生成时使用
    private String storageId;
    // 数量  原料刀拆卸时需要传递数量 默认为1
    private Integer quantity = 1;
}
