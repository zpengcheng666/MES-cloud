package com.miyu.module.wms.controller.admin.warehouse.vo;

import lombok.Data;

@Data
public class WarehouseLocationSimpleVO {
    private String id;
    private Integer type = 3;
    private String name;
    /**
     * 库区id
     */
    private String warehouseAreaId;
}
