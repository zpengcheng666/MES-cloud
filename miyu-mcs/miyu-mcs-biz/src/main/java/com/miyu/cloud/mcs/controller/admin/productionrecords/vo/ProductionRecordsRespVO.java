package com.miyu.cloud.mcs.controller.admin.productionrecords.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import java.util.*;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;
import com.alibaba.excel.annotation.*;

import javax.validation.constraints.NotEmpty;

@Schema(description = "管理后台 - 现场作业记录 Response VO")
@Data
@ExcelIgnoreUnannotated
public class ProductionRecordsRespVO {

    @Schema(description = "订单编号", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("订单编号")
    private String orderNumber;

    @Schema(description = "批次订单编码")
    private String batchNumber;

    @Schema(description = "编号")
    @ExcelProperty("编号")
    private String detailNumber;

    @Schema(description = "生产单元编号")
    @ExcelProperty("生产单元编号")
    private String processingUnitId;

    @Schema(description = "物料条码")
    private String barCode;

    @Schema(description = "加工设备", example = "31011")
    @ExcelProperty("加工设备")
    private String equipmentId;

    @Schema(description = "操作类型", example = "2")
    @ExcelProperty("操作类型")
    private Integer operationType;

    @Schema(description = "操作时间")
    @ExcelProperty("操作时间")
    private LocalDateTime operationTime;

    @Schema(description = "操作人")
    @ExcelProperty("操作人")
    private String operationBy;
    private String operationName;


    @Schema(description = "创建时间")
    @ExcelProperty("创建时间")
    private LocalDateTime createTime;

    @Schema(description = "数量")
    @ExcelProperty("报工数量")
    private String totality;

    @Schema(description = "id", requiredMode = Schema.RequiredMode.REQUIRED, example = "6199")
    private String id;

    @Schema(description = "订单id", requiredMode = Schema.RequiredMode.REQUIRED, example = "6308")
    private String orderId;

    @Schema(description = "批次订单Id", requiredMode = Schema.RequiredMode.REQUIRED, example = "18418")
    private String batchId;

    @Schema(description = "批次订单编码")
    private String batchCode;

    @Schema(description = "订单零件id")
    private String orderDetailId;

    @Schema(description = "批次零件id")
    private String batchDetailId;

    @Schema(description = "批次任务id")
    private String batchRecordId;

    @Schema(description = "工序id")
    private String processId;


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
