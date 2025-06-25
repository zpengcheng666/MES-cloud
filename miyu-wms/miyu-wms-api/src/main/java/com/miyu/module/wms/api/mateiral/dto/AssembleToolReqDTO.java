package com.miyu.module.wms.api.mateiral.dto;

import lombok.Data;

@Data
public class AssembleToolReqDTO {

    // 物料ID 必填
    private String materialStockId;
    /**
     * 物料储位
     * 指定储位：拆卸
     * 未指定：装配
     */
    private String storageId;
    // 数量
    private Integer quantity = 1;
}
