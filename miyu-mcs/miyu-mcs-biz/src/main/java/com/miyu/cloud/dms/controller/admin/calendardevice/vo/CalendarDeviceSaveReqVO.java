package com.miyu.cloud.dms.controller.admin.calendardevice.vo;

import com.miyu.cloud.dms.dal.dataobject.calendarshift.ShiftTimeDO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Schema(description = "管理后台 - 设备日历,记录设备每天的可用时间新增/修改 Request VO")
@Data
public class CalendarDeviceSaveReqVO {

    @Schema(description = "id", requiredMode = Schema.RequiredMode.REQUIRED, example = "23908")
    private String id;

    @Schema(description = "设备id", requiredMode = Schema.RequiredMode.REQUIRED, example = "2716")
    @NotEmpty(message = "设备id不能为空")
    private String deviceId;

    @Schema(description = "日期")
    private LocalDate date;

    @Schema(description = "开始时间")
    private LocalTime startTime;

    @Schema(description = "结束时间")
    private LocalTime endTime;

    @Schema(description = "日期集合,以逗号分隔的字符串")
    private String dateList;

    List<ShiftTimeDO> timeList;

}
