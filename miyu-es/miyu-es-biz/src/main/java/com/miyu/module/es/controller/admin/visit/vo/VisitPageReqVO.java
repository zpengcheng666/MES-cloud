package com.miyu.module.es.controller.admin.visit.vo;

import lombok.*;
import java.util.*;
import io.swagger.v3.oas.annotations.media.Schema;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;

import static cn.iocoder.yudao.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

@Schema(description = "管理后台 - 访客记录分页 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class VisitPageReqVO extends PageParam {

    @Schema(description = "访客姓名", example = "王五")
    private String name;

    @Schema(description = "访客签退时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] visitorCancelTime;

    @Schema(description = "访问记录状态(1：创建访客访问记录 2：取消访客访问记录 3：签到)", example = "2")
    private Integer status;

    @Schema(description = "访客单位")
    private String company;

    @Schema(description = "来访目的")
    private String cause;

    @Schema(description = "同行人数", example = "1411")
    private String followCount;

    @Schema(description = "计划开始时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] planBeginTime;

    @Schema(description = "计划结束时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] planEndTime;

    @Schema(description = "访客签到时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] visitorRecordTime;

    @Schema(description = "访客签到码")
    private String visitorCheckCode;

    @Schema(description = "被访人tpId", example = "320")
    private String visitTpId;

    @Schema(description = "设备 SN")
    private String deviceSn;

}