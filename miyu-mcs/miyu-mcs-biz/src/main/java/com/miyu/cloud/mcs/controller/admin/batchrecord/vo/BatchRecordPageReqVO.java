package com.miyu.cloud.mcs.controller.admin.batchrecord.vo;

import lombok.*;

import java.time.LocalDateTime;
import java.util.*;
import io.swagger.v3.oas.annotations.media.Schema;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import org.springframework.format.annotation.DateTimeFormat;

import static cn.iocoder.yudao.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

@Schema(description = "管理后台 - 批次工序任务分页 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class BatchRecordPageReqVO extends PageParam {

    @Schema(description = "订单id", example = "3758")
    private String orderId;

    @Schema(description = "批次id", example = "5110")
    private String batchId;

    @Schema(description = "工序Id", example = "3247")
    private String processId;

    @Schema(description = "编码")
    private String number;

    @Schema(description = "设备类型id", example = "7876")
    private String deviceTypeId;

    @Schema(description = "生产单元", example = "7876")
    private String processingUnitId;

    @Schema(description = "设备编号")
    private String deviceNumber;

    @Schema(description = "设备Id")
    private String deviceId;

    @Schema(description = "工序序号")
    private String procedureNum;

    @Schema(description = "状态", example = "1")
    private Integer status;

    @Schema(description = "预计开始时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] planStartTime;

    @Schema(description = "预计结束时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] planEndTime;

    @Schema(description = "数量", example = "18217")
    private Integer count;

    @Schema(description = "前置批次Id", example = "2935")
    private String preRecordId;

    @Schema(description = "开始时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] startTime;

    @Schema(description = "结束时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] endTime;

    @Schema(description = "创建时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] createTime;

    @Schema(description = "批量管理")
    private Boolean isBatch;
}
