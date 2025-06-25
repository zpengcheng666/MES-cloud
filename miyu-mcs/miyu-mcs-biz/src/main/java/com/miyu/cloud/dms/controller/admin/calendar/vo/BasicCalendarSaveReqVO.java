package com.miyu.cloud.dms.controller.admin.calendar.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Schema(description = "管理后台 - 基础日历新增/修改 Request VO")
@Data
public class BasicCalendarSaveReqVO {

    @Schema(description = "id", requiredMode = Schema.RequiredMode.REQUIRED, example = "20306")
    private String id;

    @Schema(description = "日历名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "王五")
    @NotEmpty(message = "日历名称不能为空")
    private String name;

    @Schema(description = "开始日期,已经没有了", requiredMode = Schema.RequiredMode.REQUIRED)
    //@NotNull(message = "开始日期不能为空")
    private LocalDateTime startDate;

    @Schema(description = "结束日期,已经没有了", requiredMode = Schema.RequiredMode.REQUIRED)
    //@NotNull(message = "结束日期不能为空")
    private LocalDateTime endDate;

}
