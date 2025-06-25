package com.miyu.cloud.mcs.controller.admin.batchorder.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import java.util.*;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;
import com.alibaba.excel.annotation.*;

@Schema(description = "管理后台 - 批次级订单 Response VO")
@Data
@ExcelIgnoreUnannotated
public class BatchOrderRespVO {

    @Schema(description = "ID", example = "1")
    private String id;

    @Schema(description = "订单id")
    private String orderId;

    @Schema(description = "订单编号", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("订单编号")
    private String orderNumber;

    @Schema(description = "批次订单编码")
    @ExcelProperty("批次订单编码")
    private String batchNumber;

    @Schema(description = "工艺id")
    private String technologyId;
    /**
     * 工艺规程版本编码
     */
    private String technologyCode;

    @Schema(description = "起始顺序号")
    private String beginProcessId;

    @Schema(description = "拆单工序")
    private String splitProcessId;

    @Schema(description = "数量", requiredMode = Schema.RequiredMode.REQUIRED, example = "23641")
    @ExcelProperty("数量")
    private Integer count;

    @Schema(description = "预计开始时间")
    @ExcelProperty("预计开始时间")
    private LocalDateTime planStartTime;

    @Schema(description = "截止日期")
    @ExcelProperty("截止日期")
    private LocalDateTime planEndTime;

    @Schema(description = "开始时间")
    @ExcelProperty("开始时间")
    private LocalDateTime startTime;

    @Schema(description = "结束时间")
    @ExcelProperty("结束时间")
    private LocalDateTime endTime;

    @Schema(description = "前置批次")
    private String preBatchId;

    @Schema(description = "前置批次编码")
    private String preBatchNumber;

    @Schema(description = "外协厂家")
    private String aidMill;

    @Schema(description = "状态")
    private Integer status;

    private Integer submitStatus;

    @Schema(description = "批量管理")
    private Boolean isBatch;

    @Schema(description = "加工类型")
    private Integer procesStatus;

    @Schema(description = "物料条码")
    private String barCode;
}
