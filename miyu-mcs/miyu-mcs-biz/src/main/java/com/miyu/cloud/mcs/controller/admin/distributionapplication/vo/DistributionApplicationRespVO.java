package com.miyu.cloud.mcs.controller.admin.distributionapplication.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import java.util.*;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;
import com.alibaba.excel.annotation.*;

@Schema(description = "管理后台 - 物料配送申请 Response VO")
@Data
@ExcelIgnoreUnannotated
public class DistributionApplicationRespVO {

    @Schema(description = "id", requiredMode = Schema.RequiredMode.REQUIRED, example = "10728")
    @ExcelProperty("id")
    private String id;

    @Schema(description = "申请单号")
    @ExcelProperty("申请单号")
    private String applicationNumber;

    @Schema(description = "申请单元", example = "6470")
    @ExcelProperty("申请单元")
    private String processingUnitId;

    @Schema(description = "订单id", example = "6470")
    private String orderId;

    @Schema(description = "订单编号", example = "6470")
    @ExcelProperty("订单编号")
    private String orderNumber;

    @Schema(description = "任务id", example = "6470")
    private String batchRecordId;

    @Schema(description = "任务编号", example = "6470")
    @ExcelProperty("任务编号")
    private String batchRecordNumber;

    @Schema(description = "申请单元", example = "6470")
    @ExcelProperty("申请单元")
    private String unitName;

    @Schema(description = "申请状态", example = "1")
    @ExcelProperty("申请状态")
    private Integer status;

    @Schema(description = "申请人")
    @ExcelProperty("申请人")
    private String creator;

    @Schema(description = "申请人名称")
    @ExcelProperty("申请人名称")
    private String creatorName;

    @Schema(description = "申请时间")
    @ExcelProperty("申请时间")
    private LocalDateTime createTime;

}
