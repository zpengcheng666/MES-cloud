package com.miyu.cloud.dms.controller.admin.calendar.detailvo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema(description = "管理后台 - 基础日历的工作日新增/修改 Request VO")
@Data
public class CalendarDetailSaveReqVO {

    @Schema(description = "id", requiredMode = Schema.RequiredMode.REQUIRED, example = "26486")
    private String id;

    @Schema(description = "基础日历id", example = "26123")
    private String bcid;

    @Schema(description = "休息/工作(1/2)", example = "1")
    private Integer cdname;

    @Schema(description = "日期")
    private String cddate;

}
