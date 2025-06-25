package com.miyu.module.ppm.controller.admin.consignmentreturndetail.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import java.util.*;
import java.math.BigDecimal;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;
import com.alibaba.excel.annotation.*;

@Schema(description = "管理后台 - 销售退货单详情 Response VO")
@Data
@ExcelIgnoreUnannotated
public class ConsignmentReturnDetailRespVO {

    @Schema(description = "ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "29375")
    @ExcelProperty("ID")
    private String id;

    @Schema(description = "退货单ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "3674")
    @ExcelProperty("退货单ID")
    private String consignmentReturnId;

    @Schema(description = "发货单详情ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "26224")
    @ExcelProperty("发货单详情ID")
    private String consignmentDetailId;

    @Schema(description = "合同订单ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "27093")
    @ExcelProperty("合同订单ID")
    private String orderId;

    @Schema(description = "退货数量", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("退货数量")
    private BigDecimal consignedAmount;

    @Schema(description = "入库数量")
    @ExcelProperty("入库数量")
    private BigDecimal inboundAmount;

    @Schema(description = "入库人")
    @ExcelProperty("入库人")
    private String inboundBy;

    @Schema(description = "出库时间")
    @ExcelProperty("出库时间")
    private LocalDateTime inboundTime;

    @Schema(description = "确认数量")
    @ExcelProperty("确认数量")
    private BigDecimal signedAmount;

    @Schema(description = "确认人")
    @ExcelProperty("确认人")
    private String signedBy;

    @Schema(description = "确认日期")
    @ExcelProperty("确认日期")
    private LocalDateTime signedTime;

    @Schema(description = "创建时间", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("创建时间")
    private LocalDateTime createTime;

    @Schema(description = "物料库存ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "4284")
    @ExcelProperty("物料库存ID")
    private String materialStockId;

    @Schema(description = "物料条码", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("物料条码")
    private String barCode;

    @Schema(description = "物料批次号", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("物料批次号")
    private String batchNumber;

}