package com.miyu.cloud.dms.controller.admin.calendardevice.provo;

import cn.iocoder.yudao.framework.common.pojo.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

import static cn.iocoder.yudao.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

@Schema(description = "管理后台 - 设备日历关联部分,记录了设备绑定了那些班次分页 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class CalendarProductionlinePageReqVO extends PageParam {

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

    @Schema(description = "创建时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] createTime;

}
