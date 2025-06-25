package com.miyu.module.wms.controller.admin.operatingterminal.vo;

import lombok.Data;

@Data
public class CutterTrayDetailInfoVO {

    // 储位Id
    private String storageId;
    // 储位编码
    private String storageCode;
    // 储位上存储的物料
    private String barCode;
}
