package com.miyu.cloud.dms.controller.admin.calendar.specialvo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Schema(description = "管理后台 - 基础日历的工作日（特别版，调休节假日等特殊日期）新增/修改 Request VO")
@Data
public class CalendarSpecialSaveReqVO {

    @Schema(description = "id", requiredMode = Schema.RequiredMode.REQUIRED, example = "30709")
    private String id;

//    @Schema(description = "基础日历id,已经没用了", example = "12632")
//    private String bcid;

    @Schema(description = "休息/工作(1/2)", example = "芋艿")
    private Integer csname;

    @Schema(description = "日期")
    private String csdate;

}
