package com.miyu.cloud.mcs.controller.admin.batchorderapsresult.vo;

import lombok.*;
import java.util.*;
import io.swagger.v3.oas.annotations.media.Schema;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;

import static cn.iocoder.yudao.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

@Schema(description = "管理后台 - 排产结果分页 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class BatchOrderApsResultPageReqVO extends PageParam {

    @Schema(description = "排产开始时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] startTime;

    @Schema(description = "排产结果")
    private String apsContent;

    @Schema(description = "状态:1新创建,2已提交,3已通过,4作废", example = "1")
    private Integer status;

    @Schema(description = "提交时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] submitedTime;

    @Schema(description = "通过时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] acceptedTime;

    @Schema(description = "审批人")
    private String acceptedBy;

    @Schema(description = "作废时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] nullifiedTime;

    @Schema(description = "作废人")
    private String nullifiedBy;

    @Schema(description = "排产方式")
    private String planPriority;

    @Schema(description = "排产结果")
    private String apsConfig;

    @Schema(description = "所属部门(这个没用吧)")
    private String sysOrgCode;

    @Schema(description = "创建时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] createTime;

}
