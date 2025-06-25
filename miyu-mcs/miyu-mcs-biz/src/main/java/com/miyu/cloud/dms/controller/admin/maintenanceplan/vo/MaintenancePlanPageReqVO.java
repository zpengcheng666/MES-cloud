package com.miyu.cloud.dms.controller.admin.maintenanceplan.vo;

import cn.iocoder.yudao.framework.common.pojo.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

import static cn.iocoder.yudao.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

@Schema(description = "管理后台 - 设备保养维护计划分页 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class MaintenancePlanPageReqVO extends PageParam {

    @Schema(description = "计划编码")
    private String code;

    @Schema(description = "所属计划关联树")
    private String tree;

    @Schema(description = "设备")
    private String device;

    @Schema(description = "是否为关键设备")
    private Integer criticalDevice;

    @Schema(description = "启用状态", example = "1")
    private Integer enableStatus;

    @Schema(description = "是否超期停机", example = "2")
    private Integer expirationShutdown;

    @Schema(description = "超期时间")
    private Integer expirationTime;

    @Schema(description = "维护类型", example = "1")
    private Integer type;

    @Schema(description = "开始时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] startTime;

    @Schema(description = "cron表达式")
    private String cornExpression;

    @Schema(description = "维护内容")
    private String content;

    @Schema(description = "说明", example = "你猜")
    private String remark;

    @Schema(description = "负责人")
    private String superintendent;

    @Schema(description = "最后一次保养时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] lastTime;

    @Schema(description = "上一次完成状态", example = "1")
    private Integer lastStatus;

    @Schema(description = "创建时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] createTime;

}
