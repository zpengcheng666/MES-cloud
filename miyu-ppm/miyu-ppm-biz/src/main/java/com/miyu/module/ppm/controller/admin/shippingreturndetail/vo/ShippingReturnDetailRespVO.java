package com.miyu.module.ppm.controller.admin.shippingreturndetail.vo;

import com.baomidou.mybatisplus.annotation.TableField;
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
public class ShippingReturnDetailRespVO {

    @Schema(description = "ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "4124")
    @ExcelProperty("ID")
    private String id;

    @Schema(description = "退货单ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "17113")
    @ExcelProperty("退货单ID")
    private String shippingReturnId;

    @Schema(description = "发货单详情ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "17113")
    @ExcelProperty("发货单详情ID")
    private String shippingDetailId;

    @Schema(description = "合同订单ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "16163")
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

    @Schema(description = "物料库存ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "6074")
    @ExcelProperty("物料库存ID")
    private String materialStockId;

    @Schema(description = "物料条码", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("物料条码")
    private String barCode;

    @Schema(description = "物料批次号", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("物料批次号")
    private String batchNumber;


    private String materialConfigId;
    @Schema(description = "物料编号")
    private String materialNumber;

    @Schema(description = "物料类码")
    private String materialCode;

    @Schema(description = "物料名称", example = "张三")
    private String materialName;

    @Schema(description = "物料属性（成品、毛坯、辅助材料）",requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer materialProperty;

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



    /***
     * 发货单
     */
    @Schema(description = "发货单")
    private String shippingNo;
    /***
     * 发货单名称
     */
    @Schema(description = "发货单名称")
    private String shippingName;
}