package com.miyu.cloud.mcs.controller.admin.batchrecord.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDateTime;
import java.util.*;
import javax.validation.constraints.*;

@Schema(description = "管理后台 - 批次工序任务新增/修改 Request VO")
@Data
public class BatchRecordSaveReqVO {

    @Schema(description = "id", requiredMode = Schema.RequiredMode.REQUIRED, example = "3532")
    private String id;

    @Schema(description = "订单id", example = "3758")
    private String orderId;

    @Schema(description = "批次id", requiredMode = Schema.RequiredMode.REQUIRED, example = "5110")
    @NotEmpty(message = "批次id不能为空")
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

    @Schema(description = "状态", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @NotNull(message = "状态不能为空")
    private Integer status;

    @Schema(description = "预计开始时间")
    private LocalDateTime planStartTime;

    @Schema(description = "预计结束时间")
    private LocalDateTime planEndTime;

    @Schema(description = "数量", requiredMode = Schema.RequiredMode.REQUIRED, example = "18217")
    @NotNull(message = "数量不能为空")
    private Integer count;

    @Schema(description = "前置批次Id", example = "2935")
    private String preRecordId;

    @Schema(description = "开始时间")
    private LocalDateTime startTime;

    @Schema(description = "结束时间")
    private LocalDateTime endTime;

    @Schema(description = "批量管理")
    private Boolean isBatch;
}
