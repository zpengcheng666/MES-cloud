package com.miyu.module.ppm.controller.admin.contract.vo;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import com.alibaba.excel.annotation.*;

@Schema(description = "管理后台 - 合同订单 Response VO")
@Data
@ExcelIgnoreUnannotated
public class ContractOrderRespVO {

    @Schema(description = "ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "15782")
    @ExcelProperty("ID")
    private String id;

    @Schema(description = "合同ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "18620")
    @ExcelProperty("合同ID")
    private String contractId;

    @Schema(description = "产品ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "20650")
    @ExcelProperty("产品ID")
    private String materialConfigId;

    @Schema(description = "数量", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("数量")
    private BigDecimal quantity;

    @Schema(description = "单价", requiredMode = Schema.RequiredMode.REQUIRED, example = "19985")
    @ExcelProperty("单价")
    private BigDecimal price;

    @Schema(description = "税率", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("税率")
    private BigDecimal taxRate;

    @Schema(description = "含税单价", requiredMode = Schema.RequiredMode.REQUIRED, example = "6658")
    @ExcelProperty("含税单价")
    private BigDecimal taxPrice;

    @Schema(description = "交货日期", requiredMode = Schema.RequiredMode.REQUIRED)
    private LocalDateTime leadDate;

    @Schema(description = "已发数量", example = "1")
    private BigDecimal finishQuantity;



    @Schema(description = "物料编号")
    private String materialNumber;

    @Schema(description = "物料类码")
    private String materialCode;

    @Schema(description = "物料名称", example = "张三")
    private String materialName;

    @Schema(description = "物料属性（成品、毛坯、辅助材料）",requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer materialProperty;

    @Schema(description = "物料类型（零件、托盘、工装、夹具、刀具）",requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    private String materialType;

    @Schema(description = "物料规格")
    private String materialSpecification;

    @Schema(description = "物料品牌")
    private String materialBrand;

    @Schema(description = "物料单位")
    private String materialUnit;

    private String productName;

    private String avgPrice;

    private String maxPrice;

    private String minPrice;

    private String latestPrice;

    private String initTax;

    @Schema(description = "项目ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "123.000")
    private String projectId;

    @Schema(description = "项目订单ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "123.000")
    private String orderId;

    @Schema(description = "项目子计划ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "123.000")
    private String projectPlanId;

    private String projectPlanItemId;

    @Schema(description = "合同编号")
    private String number;

    @Schema(description = "合同名称")
    private String name;

    @Schema(description = "合同类别")
    private String contractType;

    @Schema(description = "合同方")
    private String partyName;

    @Schema(description = "合同状态")
    private Integer status;
}
