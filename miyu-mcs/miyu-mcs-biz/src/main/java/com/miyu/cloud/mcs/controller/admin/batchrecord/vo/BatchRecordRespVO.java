package com.miyu.cloud.mcs.controller.admin.batchrecord.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDateTime;
import java.util.*;
import java.util.*;
import com.alibaba.excel.annotation.*;

@Schema(description = "管理后台 - 批次工序任务 Response VO")
@Data
@ExcelIgnoreUnannotated
public class BatchRecordRespVO {

    @Schema(description = "id", requiredMode = Schema.RequiredMode.REQUIRED, example = "3532")
    @ExcelProperty("id")
    private String id;

    @Schema(description = "订单id", example = "3758")
    @ExcelProperty("订单id")
    private String orderId;

    @Schema(description = "零件图号", example = "3758")
    @ExcelProperty("零件图号")
    private String partNumber;

    @Schema(description = "批次id", requiredMode = Schema.RequiredMode.REQUIRED, example = "5110")
    @ExcelProperty("批次id")
    private String batchId;

    @Schema(description = "工序Id", example = "3247")
    private String processId;

    @Schema(description = "编码")
    @ExcelProperty("编码")
    private String number;

    @Schema(description = "设备类型id", example = "7876")
    private String deviceTypeId;

    @Schema(description = "生产单元id", example = "7876")
    @ExcelProperty("生产单元id")
    private String processingUnitId;

    @Schema(description = "生产单元", example = "7876")
    @ExcelProperty("生产单元")
    private String unitName;

    @Schema(description = "设备编号")
    @ExcelProperty("设备编号")
    private String deviceNumber;

    @Schema(description = "设备Id")
    private String deviceId;

    @Schema(description = "工序序号")
    @ExcelProperty("工序序号")
    private String procedureNum;

    @Schema(description = "状态", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @ExcelProperty("状态")
    private Integer status;

    @Schema(description = "预计开始时间")
    @ExcelProperty("预计开始时间")
    private LocalDateTime planStartTime;

    @Schema(description = "预计结束时间")
    @ExcelProperty("预计结束时间")
    private LocalDateTime planEndTime;

    @Schema(description = "数量", requiredMode = Schema.RequiredMode.REQUIRED, example = "18217")
    @ExcelProperty("数量")
    private Integer count;

    @Schema(description = "前置批次Id", example = "2935")
    @ExcelProperty("前置批次Id")
    private String preRecordId;

    @Schema(description = "开始时间")
    @ExcelProperty("开始时间")
    private LocalDateTime startTime;

    @Schema(description = "结束时间")
    @ExcelProperty("结束时间")
    private LocalDateTime endTime;

    @Schema(description = "创建时间")
    @ExcelProperty("创建时间")
    private LocalDateTime createTime;

    @Schema(description = "批量管理")
    private Boolean isBatch;

    @Schema(description = "加工状态(1本厂加工 2整单外协)")
    private Integer procesStatus;

    @Schema(description = "外协厂家")
    private String aidMill;

}
