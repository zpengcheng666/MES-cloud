package com.miyu.module.ppm.controller.admin.shippinginfo.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import java.util.*;
import java.math.BigDecimal;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;
import com.alibaba.excel.annotation.*;

@Schema(description = "管理后台 - 销售发货产品 Response VO")
@Data
@ExcelIgnoreUnannotated
public class ShippingInfoRespVO {

    @Schema(description = "ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "27144")
    @ExcelProperty("ID")
    private String id;

    @Schema(description = "发货单ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "17916")
    @ExcelProperty("发货单ID")
    private String shippingId;

    @Schema(description = "合同ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "23817")
    @ExcelProperty("合同ID")
    private String projectId;

    @Schema(description = "合同订单ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "12832")
    @ExcelProperty("合同订单ID")
    private String orderId;

    @Schema(description = "合同ID", example = "10988")
    @ExcelProperty("合同ID")
    private String contractId;

    @Schema(description = "发货数量", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("发货数量")
    private BigDecimal consignedAmount;

    @Schema(description = "出库数量")
    @ExcelProperty("出库数量")
    private Long outboundAmount;

    @Schema(description = "出库人")
    @ExcelProperty("出库人")
    private String outboundBy;

    @Schema(description = "出库时间")
    @ExcelProperty("出库时间")
    private LocalDateTime outboundTime;

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

    @Schema(description = "物料类型", example = "16657")
    @ExcelProperty("物料类型")
    private String materialConfigId;

    @Schema(description = "状态  0已创建 1审批中 2 待出库 3出库中4发货确认5结束 9作废8审批失败", example = "2")
    @ExcelProperty("状态  0已创建 1审批中 2 待出库 3出库中4发货确认5结束 9作废8审批失败")
    private Integer status;


    private String consignmentId;
}