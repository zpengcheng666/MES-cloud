package com.miyu.module.ppm.controller.admin.purchaseconsignmentdetail.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import java.util.*;
import java.math.BigDecimal;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;
import com.alibaba.excel.annotation.*;

@Schema(description = "管理后台 - 收货明细 Response VO")
@Data
@ExcelIgnoreUnannotated
public class PurchaseConsignmentDetailRespVO {

    @Schema(description = "ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "9313")
    @ExcelProperty("ID")
    private String id;

    @Schema(description = "收货单ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "9994")
    @ExcelProperty("收货单ID")
    private String consignmentId;

    @Schema(description = "合同ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "8204")
    @ExcelProperty("合同ID")
    private String contractId;

    @Schema(description = "合同订单ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "23497")
    @ExcelProperty("合同订单ID")
    private String orderId;

    @Schema(description = "发货数量", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("发货数量")
    private BigDecimal consignedAmount;


    @Schema(description = "签收数量")
    @ExcelProperty("签收数量")
    private BigDecimal signedAmount;

    @Schema(description = "签收人")
    @ExcelProperty("签收人")
    private String signedBy;

    @Schema(description = "签收日期")
    @ExcelProperty("签收日期")
    private LocalDateTime signedTime;

    @Schema(description = "创建日期", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("创建日期")
    private LocalDateTime createTime;

    @Schema(description = "物料类型Id", requiredMode = Schema.RequiredMode.REQUIRED)
    private String materialConfigId;

    @Schema(description = "数量", example = "1")
    private BigDecimal quantity;


    @Schema(description = "已发数量", example = "1")
    private BigDecimal finishQuantity;

    private BigDecimal chooseQuantity;
    private BigDecimal remainingQuantity;

    /***
     * 收货单编号
     */
    private String no;
    /***
     * 收货单名称
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


    private Integer qualityCheck;



    @Schema(description = "当前类名称")
    private String materialTypeName;
    /**
     * 主类别（工件、托盘、工装、夹具、刀具）
     */
    @Schema(description = "主类别名")
    private String materialParentTypeName;



    private String projectId;
    private String projectOrderId;
    private String projectPlanId;
    private String projectPlanItemId;



    private String shippingId;
    private String shippingDetailId;
    private String shippingName;
    private String shippingNo;


    private Integer consignmentType;
    private Integer rowDisable;



    private Integer materialStatus;


    @Schema(description = "严重等级")
    private Integer defectiveLevel;

    @Schema(description = "处理方式")
    private Integer handleMethod;
    @Schema(description = "状态")
    private Integer consignmentStatus;
    
}