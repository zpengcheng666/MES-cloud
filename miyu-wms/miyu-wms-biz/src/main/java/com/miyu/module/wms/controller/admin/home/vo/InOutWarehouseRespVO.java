package com.miyu.module.wms.controller.admin.home.vo;

import lombok.Data;

@Data
public class InOutWarehouseRespVO {
    private String orderNumber;
    private String orderType;
    private String startWarehouseName;
    private String targetWarehouseName;
    private String materielNumber;
    private String barCode;
}
