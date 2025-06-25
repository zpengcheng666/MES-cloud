package com.miyu.module.tms.controller.admin.assembletask.vo;

import lombok.*;
import java.util.*;
import io.swagger.v3.oas.annotations.media.Schema;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;

import static cn.iocoder.yudao.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

@Schema(description = "管理后台 - 刀具装配任务分页 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class AssembleTaskPageReqVO extends PageParam {

    @Schema(description = "ID", requiredMode = Schema.RequiredMode.REQUIRED)
    private String id;

    @Schema(description = "创建时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] createTime;

    @Schema(description = "接单人")
    private String operator;

    @Schema(description = "工单号")
    private String orderNumber;

    @Schema(description = "目标位置")
    private String targetLocation;

    @Schema(description = "物料类型id")
    private String materialConfigId;

    @Schema(description = "刀具主表id")
    private String toolInfoId;

    @Schema(description = "状态（启用、作废）")
    private Integer status;

}