package com.miyu.module.wms.controller.admin.operatingterminal.vo;

import lombok.Data;


@Data
public class CutterOrderVO {

    // 订单类型
    private String orderType;
    // 刀具编号
    private String materialNumber;
    // 刀具名称
    private String materialName;
    // 刀具物码
    private String barCode;
    // 目标仓库
    private String targetWarehouseName;
    // 目标仓库
    private String targetLocationName;
    // 数量 必填
    private Integer quantity;
    // 所在库位编码   也可能是储位
    private String locationName;

}
