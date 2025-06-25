package com.miyu.module.wms.controller.admin.operatingterminal.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema(description = "管理后台 - 托盘信息 VO")
@Data
public class TrayInfoVO {
    private String locationId;
    private String locationName;
    private String materialNumber;
    private String barCode;
    private String materialStockId;
    // 是否单储位
    private Boolean materialStorage;
    // 是否锁定
    private Boolean locationLocked;
}
