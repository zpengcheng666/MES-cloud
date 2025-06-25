package com.miyu.cloud.mcs.controller.admin.distributionapplication.vo;

import lombok.*;
import java.util.*;
import io.swagger.v3.oas.annotations.media.Schema;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;

import static cn.iocoder.yudao.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

@Schema(description = "管理后台 - 物料配送申请分页 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class DistributionApplicationPageReqVO extends PageParam {

    private String id;

    @Schema(description = "申请单号")
    private String applicationNumber;

    @Schema(description = "申请单元", example = "6470")
    private String processingUnitId;

    @Schema(description = "订单id", example = "6470")
    private String orderId;

    @Schema(description = "订单编号", example = "6470")
    private String orderNumber;

    @Schema(description = "任务id", example = "6470")
    private String batchRecordId;

    @Schema(description = "任务编号", example = "6470")
    private String batchRecordNumber;

    @Schema(description = "申请状态", example = "1")
    private Integer status;

}
