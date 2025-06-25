package com.miyu.module.ppm.controller.admin.shippingdetail.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import java.util.*;
import java.math.BigDecimal;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;
import com.alibaba.excel.annotation.*;

@Schema(description = "管理后台 - 销售发货明细 Response VO")
@Data
@ExcelIgnoreUnannotated
public class ShippingDetailRespVO {

    @Schema(description = "ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "27513")
    @ExcelProperty("ID")
    private String id;

    @Schema(description = "发货单ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "8204")
    @ExcelProperty("发货单ID")
    private String shippingId;

    @Schema(description = "合同ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "8204")
    @ExcelProperty("合同ID")
    private String contractId;

    @Schema(description = "合同订单ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "2595")
    @ExcelProperty("合同订单ID")
    private String orderId;

    private String projectId;

    @Schema(description = "发货数量", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("发货数量")
    private BigDecimal consignedAmount;

    /**
     * 出库人
     */
    @Schema(description = "出库人", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("出库人")
    private String outboundBy;

    @Schema(description = "出库数量", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("出库数量")
    private BigDecimal outboundAmount;

    @Schema(description = "出库时间", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("出库时间")
    private LocalDateTime outboundTime;

    @Schema(description = "确认数量")
    @ExcelProperty("确认数量")
    private BigDecimal signedAmount;

    @Schema(description = "确认")
    @ExcelProperty("确认")
    private String signedBy;

    @Schema(description = "确认日期")
    @ExcelProperty("确认日期")
    private LocalDateTime signedTime;


    @Schema(description = "物料类型Id", requiredMode = Schema.RequiredMode.REQUIRED)
    private String materialConfigId;

    @Schema(description = "数量", example = "1")
    private BigDecimal quantity;


    @Schema(description = "已发数量", example = "1")
    private BigDecimal finishQuantity;

    private BigDecimal chooseQuantity;
    private BigDecimal remainingQuantity;

    /***
     * 发货单编号
     */
    private String no;
    /***
     * 发货单名称
     */
    private String name;

    /**
     * 物料库存ID
     */
    private String materialStockId;
    /**
     * 物料条码
     */
    private String barCode;
    /**
     * 物料批次号
     */
    private String batchNumber;

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

    private Integer shippingStatus;


    /***
     * 项目订单ID
     */
    private String projectOrderId;


    private String projectPlanId;


    private String projectPlanItemId;


    private String consignmentId;
    private String consignmentNo;
    private String consignmentName;
    private String consignmentDetailId;

    private String shippingType;

}
