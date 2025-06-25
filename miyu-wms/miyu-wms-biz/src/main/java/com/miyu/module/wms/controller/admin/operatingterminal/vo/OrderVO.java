package com.miyu.module.wms.controller.admin.operatingterminal.vo;

import lombok.Data;


@Data
public class OrderVO {

    // 订单类型
    private String orderType;
    // 物料批次号
    private String batchNumber;
    // 物料编号
    private String materialNumber;
    // 物料选择条码
    private String chooseBarCode;
    // 数量 必填
    private Integer quantity;

}
