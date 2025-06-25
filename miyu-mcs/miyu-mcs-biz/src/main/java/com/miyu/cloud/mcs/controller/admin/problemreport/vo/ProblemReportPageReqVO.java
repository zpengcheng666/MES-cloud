package com.miyu.cloud.mcs.controller.admin.problemreport.vo;

import cn.iocoder.yudao.framework.common.pojo.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

import static cn.iocoder.yudao.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

@Schema(description = "管理后台 - 问题上报分页 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class ProblemReportPageReqVO extends PageParam {

    @Schema(description = "工位id")
    private String stationId;

    @Schema(description = "问题类型", example = "1")
    private Integer type;

    @Schema(description = "上报id", example = "14910")
    private String reportId;

    @Schema(description = "状态", example = "1")
    private String status;

    @Schema(description = "问题描述")
    private String content;

    @Schema(description = "创建时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] createTime;

}