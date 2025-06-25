package com.miyu.cloud.dms.controller.admin.calendardevice.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 关联班次绑定用
 */
@Schema(description = "管理后台 - 设备日历接收类")
@Data
public class CalendarDeviceReqVO {

    @Schema(description = "基础日历id", example = "芋艿")
    private String bcid;

    @Schema(description = "设备id", example = "25921")
    private String deviceId;

    @Schema(description = "设备编号")
    private String deviceCode;

    @Schema(description = "设备名称", example = "芋艿")
    private String deviceName;

    @Schema(description = "班次id", example = "32660")
    private String shiftId;

    @Schema(description = "班次名称", example = "芋艿")
    private String shiftName;

}
