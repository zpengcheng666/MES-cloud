package com.miyu.cloud.mcs.controller.admin.receiptrecord.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import java.util.*;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;
import com.alibaba.excel.annotation.*;

@Schema(description = "管理后台 - 生产单元签收记录 Response VO")
@Data
@ExcelIgnoreUnannotated
public class ReceiptRecordRespVO {

    @Schema(description = "id", requiredMode = Schema.RequiredMode.REQUIRED, example = "23634")
    @ExcelProperty("id")
    private String id;

    @Schema(description = "申请id", requiredMode = Schema.RequiredMode.REQUIRED, example = "7789")
    @ExcelProperty("申请id")
    private String applicationId;

    @Schema(description = "配送详情id", requiredMode = Schema.RequiredMode.REQUIRED, example = "7789")
    private String distributionRecordId;

    @Schema(description = "需求详情id")
    private String demandRecordId;

    @Schema(description = "物料id")
    private String materialUid;

    @Schema(description = "物料批次编码")
    private String batchNumber;

    @Schema(description = "物料条码")
    private String barCode;

    @Schema(description = "申请编码")
    @ExcelProperty("申请编码")
    private String applicationNumber;;

    @Schema(description = "申请单元", example = "242")
    @ExcelProperty("申请单元")
    private String processingUnitId;

    @Schema(description = "申请单元名称")
    @ExcelProperty("申请单元名称")
    private String unitName;

    @Schema(description = "资源类型", example = "2")
    @ExcelProperty("资源类型")
    private String resourceType;

    @Schema(description = "资源类码")
    @ExcelProperty("资源类码")
    private String resourceTypeCode;

    @Schema(description = "资源编码")
    @ExcelProperty("资源编码")
    private String resource;

    @Schema(description = "需求数量", example = "29024")
    @ExcelProperty("需求数量")
    private Integer count;

    @Schema(description = "签收人")
    @ExcelProperty("签收人")
    private String creator;
    private String creatorName;

    @Schema(description = "签收时间")
    @ExcelProperty("签收时间")
    private LocalDateTime createTime;

}
