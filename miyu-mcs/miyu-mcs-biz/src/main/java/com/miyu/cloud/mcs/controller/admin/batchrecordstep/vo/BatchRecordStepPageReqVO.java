package com.miyu.cloud.mcs.controller.admin.batchrecordstep.vo;

import lombok.*;
import java.util.*;
import io.swagger.v3.oas.annotations.media.Schema;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;

import static cn.iocoder.yudao.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

@Schema(description = "管理后台 - 工步计划分页 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class BatchRecordStepPageReqVO extends PageParam {

    @Schema(description = "工序任务id", example = "21877")
    private String batchRecordId;

    @Schema(description = "工步id", example = "16737")
    private String stepId;

    @Schema(description = "工步名称", example = "王五")
    private String stepName;

    @Schema(description = "工步顺序号")
    private String stepOrder;

    @Schema(description = "设备类型", example = "17848")
    private String deviceTypeId;

    @Schema(description = "指定设备id集合")
    private String defineDeviceIds;

    @Schema(description = "计划开始时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] planStartTime;

    @Schema(description = "计划结束时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] planEndTime;

    @Schema(description = "创建时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] createTime;

}
