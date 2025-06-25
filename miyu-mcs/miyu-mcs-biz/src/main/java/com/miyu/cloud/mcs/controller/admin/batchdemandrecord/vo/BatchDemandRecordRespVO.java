package com.miyu.cloud.mcs.controller.admin.batchdemandrecord.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import java.util.*;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;
import com.alibaba.excel.annotation.*;

@Schema(description = "管理后台 - 需求分拣详情 Response VO")
@Data
@ExcelIgnoreUnannotated
public class BatchDemandRecordRespVO {

    @Schema(description = "id", requiredMode = Schema.RequiredMode.REQUIRED, example = "11150")
    @ExcelProperty("id")
    private String id;

    @Schema(description = "物料id", example = "3360")
    @ExcelProperty("物料id")
    private String materialUid;

    @Schema(description = "批次id", example = "3360")
    @ExcelProperty("批次id")
    private String batchId;

    @Schema(description = "批次任务id")
    private String batchRecordId;

    //批次任务编码
    private String batchRecordNumber;

    //资源类型(仓储)
    private String resourceType;

    @Schema(description = "需求id", example = "3360")
    @ExcelProperty("需求id")
    private String demandId;

    @Schema(description = "物料类型id", example = "27367")
    @ExcelProperty("物料类型id")
    private String materialConfigId;

    @Schema(description = "物料类型编号")
    @ExcelProperty("物料类型编号")
    private String materialNumber;

    @Schema(description = "物料条码")
    @ExcelProperty("物料条码")
    private String barCode;

    @Schema(description = "物料批次")
    @ExcelProperty("物料批次")
    private String batchNumber;

    @Schema(description = "批次编码")
    private String batchCode;

    @Schema(description = "数量")
    @ExcelProperty("数量")
    private Integer totality;

    @Schema(description = "是否为批量")
    @ExcelProperty("是否为批量")
    private Integer batch;

    @Schema(description = "配送状态")
    @ExcelProperty("配送状态")
    private Integer status;

    @Schema(description = "创建时间")
    @ExcelProperty("创建时间")
    private LocalDateTime createTime;

    @Schema(description = "批次编码")
    private String processingUnitId;
    private String unitNumber;

    @Schema(description = "批次编码")
    private String deviceId;
    private String deviceNumber;

}
