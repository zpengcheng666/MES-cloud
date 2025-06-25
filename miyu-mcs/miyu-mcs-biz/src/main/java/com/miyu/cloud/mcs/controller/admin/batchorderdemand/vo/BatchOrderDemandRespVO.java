package com.miyu.cloud.mcs.controller.admin.batchorderdemand.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import java.util.*;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;
import com.alibaba.excel.annotation.*;

@Schema(description = "管理后台 - 批次订单需求 Response VO")
@Data
@ExcelIgnoreUnannotated
public class BatchOrderDemandRespVO {

    @Schema(description = "ID", example = "1")
    private String id;

    private String orderId;

    @Schema(description = "批次首单Id")
    private String firstBatchId;

    @Schema(description = "订单编号", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("订单编号")
    private String orderNumber;

    private String batchId;

    @Schema(description = "批次任务id")
    private String batchRecordId;

    @Schema(description = "批次订单编码")
    @ExcelProperty("批次订单编码")
    private String batchNumber;

    @Schema(description = "资源类型", example = "2")
    @ExcelProperty("资源类型")
    private String resourceType;

    @Schema(description = "资源编码")
    @ExcelProperty("资源编码")
    private String resourceTypeCode;

    private String resourceTypeId;

    @Schema(description = "需求类别")
    @ExcelProperty("需求类别")
    private Integer requirementType;

    @Schema(description = "需求数量")
    @ExcelProperty("需求数量")
    private Integer total;

    @Schema(description = "最小需求数量")
    @ExcelProperty("最小需求数量")
    private Integer minimum;

    @Schema(description = "齐备情况", example = "1")
    @ExcelProperty("齐备情况")
    private Integer status;

    private String confirmedBy;

    @Schema(description = "确认人")
    @ExcelProperty("确认人")
    private String confirmedByName;

    @Schema(description = "确认时间")
    @ExcelProperty("确认时间")
    private LocalDateTime confirmedTime;

    @Schema(description = "生成单元Id")
    private String processingUnitId;

    @Schema(description = "生成单元")
    @ExcelProperty("生成单元")
    private String unitName;

    @Schema(description = "指定物料编码,逗号分割")
    private String materialCode;

}
