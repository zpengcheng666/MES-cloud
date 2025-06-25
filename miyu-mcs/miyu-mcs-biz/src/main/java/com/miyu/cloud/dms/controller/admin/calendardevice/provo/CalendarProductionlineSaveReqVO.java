package com.miyu.cloud.dms.controller.admin.calendardevice.provo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema(description = "管理后台 - 设备日历关联部分,记录了设备绑定了那些班次新增/修改 Request VO")
@Data
public class CalendarProductionlineSaveReqVO {

    @Schema(description = "id", requiredMode = Schema.RequiredMode.REQUIRED, example = "15996")
    private String id;

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
