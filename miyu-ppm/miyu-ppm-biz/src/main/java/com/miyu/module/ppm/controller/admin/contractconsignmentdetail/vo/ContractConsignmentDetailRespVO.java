package com.miyu.module.ppm.controller.admin.contractconsignmentdetail.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import java.util.*;
import java.math.BigDecimal;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;
import com.alibaba.excel.annotation.*;

@Schema(description = "管理后台 - 外协发货单详情 Response VO")
@Data
@ExcelIgnoreUnannotated
public class ContractConsignmentDetailRespVO {

    @Schema(description = "ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "1381")
    @ExcelProperty("ID")
    private String id;

    @Schema(description = "发货单ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "3674")
    @ExcelProperty("发货单ID")
    private String consignmentId;

    @Schema(description = "发货数量", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("发货数量")
    private BigDecimal consignedAmount;

    @Schema(description = "出库数量")
    @ExcelProperty("出库数量")
    private BigDecimal inboundAmount;

    @Schema(description = "出库人")
    @ExcelProperty("出库人")
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

    @Schema(description = "物料库存ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "29725")
    @ExcelProperty("物料库存ID")
    private String materialStockId;

    @Schema(description = "物料条码", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("物料条码")
    private String barCode;

    @Schema(description = "物料批次号", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("物料批次号")
    private String batchNumber;

    @Schema(description = "物料类型ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "10262")
    @ExcelProperty("物料类型ID")
    private String materialConfigId;

    @Schema(description = "项目ID", example = "4872")
    @ExcelProperty("项目ID")
    private String projectId;

    @Schema(description = "项目订单ID", example = "5156")
    @ExcelProperty("项目订单ID")
    private String projectOrderId;

    @Schema(description = "项目子计划ID", example = "4375")
    @ExcelProperty("项目子计划ID")
    private String projectPlanId;

    @Schema(description = "合同订单ID", example = "19499")
    @ExcelProperty("合同订单ID")
    private String orderId;
    @Schema(description = "物料编号")
    private String materialNumber;

    @Schema(description = "物料类码")
    private String materialCode;

    @Schema(description = "物料名称", example = "张三")
    private String materialName;

    @Schema(description = "当前类名称")
    private String materialTypeName;
    /**
     * 主类别（工件、托盘、工装、夹具、刀具）
     */
    @Schema(description = "主类别名")
    private String materialParentTypeName;

    @Schema(description = "物料类型（零件、托盘、工装、夹具、刀具）",requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    private String materialTypeId;

    @Schema(description = "物料规格")
    private String materialSpecification;

    @Schema(description = "物料品牌")
    private String materialBrand;

    @Schema(description = "物料单位")
    private String materialUnit;

    private String projectPlanItemId;
}