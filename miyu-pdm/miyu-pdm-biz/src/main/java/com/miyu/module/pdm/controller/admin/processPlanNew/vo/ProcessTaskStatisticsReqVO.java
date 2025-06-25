package com.miyu.module.pdm.controller.admin.processPlanNew.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

import static cn.iocoder.yudao.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;


@Schema(description = "pdm - 工艺任务统计分页 Request VO")
@Data
public class ProcessTaskStatisticsReqVO {

    @Schema(description = "id", example = "1")
    private String id;

    @Schema(description = "负责人")
    private String reviewedBy;

    @Schema(description = "负责人Id")
    private String reviewer;

    @Schema(description = "起始时间", requiredMode = Schema.RequiredMode.REQUIRED, example = "2024-08-30 00:00:00")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime[] updateTime;

}
