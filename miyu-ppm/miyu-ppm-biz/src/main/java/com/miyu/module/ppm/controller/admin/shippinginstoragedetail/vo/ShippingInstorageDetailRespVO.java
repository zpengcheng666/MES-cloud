package com.miyu.module.ppm.controller.admin.shippinginstoragedetail.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import java.util.*;
import java.math.BigDecimal;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;
import com.alibaba.excel.annotation.*;

@Schema(description = "管理后台 - 销售订单入库明细 Response VO")
@Data
@ExcelIgnoreUnannotated
public class ShippingInstorageDetailRespVO {

    @Schema(description = "ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "28936")
    @ExcelProperty("ID")
    private String id;

    @Schema(description = "收货单ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "10542")
    @ExcelProperty("收货单ID")
    private String shippingStorageId;

    @Schema(description = "项目ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "18333")
    @ExcelProperty("项目ID")
    private String projectId;

    @Schema(description = "项目订单ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "6295")
    @ExcelProperty("项目订单ID")
    private String orderId;

    @Schema(description = "创建时间", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("创建时间")
    private LocalDateTime createTime;

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

    private String materialId;

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

}