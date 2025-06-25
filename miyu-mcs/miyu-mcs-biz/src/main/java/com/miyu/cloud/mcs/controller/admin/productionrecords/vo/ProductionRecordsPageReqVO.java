package com.miyu.cloud.mcs.controller.admin.productionrecords.vo;

import lombok.*;
import java.util.*;
import io.swagger.v3.oas.annotations.media.Schema;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotEmpty;
import java.time.LocalDateTime;

import static cn.iocoder.yudao.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

@Schema(description = "管理后台 - 现场作业记录分页 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class ProductionRecordsPageReqVO extends PageParam {

    @Schema(description = "订单id", example = "6308")
    private String orderId;

    @Schema(description = "编号")
    private String detailNumber;

    @Schema(description = "订单编号")
    private String orderNumber;

    @Schema(description = "批次订单Id", example = "18418")
    private String batchId;

    @Schema(description = "批次订单编码")
    private String batchCode;

    @Schema(description = "生产单元编号")
    private String processingUnitId;

    @Schema(description = "加工设备", example = "31011")
    private String equipmentId;

    @Schema(description = "操作类型", example = "2")
    private Integer operationType;

    @Schema(description = "物料条码")
    private String barCode;

    @Schema(description = "操作时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] operationTime;

    @Schema(description = "操作人")
    private String operationBy;

    @Schema(description = "创建时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] createTime;

    @Schema(description = "数量")
    private String totality;

    @Schema(description = "id", requiredMode = Schema.RequiredMode.REQUIRED, example = "6199")
    private String id;

    @Schema(description = "订单零件id")
    private String orderDetailId;

    @Schema(description = "批次零件id")
    private String batchDetailId;

    @Schema(description = "批次任务id")
    private String batchRecordId;

    @Schema(description = "工序id")
    private String processId;

    @Schema(description = "批次编码")
    private String batchNumber;

    @Schema(description = "订单零件编码")
    private String orderDetailNumber;

    @Schema(description = "批次零件编码")
    private String batchDetailNumber;

    @Schema(description = "工序名称")
    private String processName;

    @Schema(description = "工步名称")
    private String stepName;

    @Schema(description = "单元名称")
    private String unitName;

    @Schema(description = "设备名称")
    private String deviceName;

}
