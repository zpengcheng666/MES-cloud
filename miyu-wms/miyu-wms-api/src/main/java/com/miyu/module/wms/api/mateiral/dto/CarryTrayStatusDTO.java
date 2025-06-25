package com.miyu.module.wms.api.mateiral.dto;

import com.miyu.module.wms.enums.DictConstants;
import lombok.Data;

@Data
public class CarryTrayStatusDTO {
    //呼叫托盘搬运状态 1 未知，2 搬运中，3 已抵达
    private Integer status = DictConstants.WMS_CARRY_TRAY_STATUS_UNKNOWN;
    private String trayId;
    private String startWarehouseId;
}
