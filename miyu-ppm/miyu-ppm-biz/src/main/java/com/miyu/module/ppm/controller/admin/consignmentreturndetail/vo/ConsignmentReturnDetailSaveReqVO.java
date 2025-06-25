package com.miyu.module.ppm.controller.admin.consignmentreturndetail.vo;

import com.alibaba.excel.annotation.ExcelProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import javax.validation.constraints.*;
import java.math.BigDecimal;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;

@Schema(description = "管理后台 - 销售退货单详情新增/修改 Request VO")
@Data
public class ConsignmentReturnDetailSaveReqVO {

    @Schema(description = "ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "29375")
    private String id;

    @Schema(description = "退货单ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "3674")
    @NotEmpty(message = "退货单ID不能为空")
    private String consignmentReturnId;

    @Schema(description = "发货单详情ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "26224")
    @NotEmpty(message = "发货单详情ID不能为空")
    private String consignmentDetailId;

    @Schema(description = "合同订单ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "27093")
    @NotEmpty(message = "合同订单ID不能为空")
    private String orderId;

    @Schema(description = "退货数量", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "退货数量不能为空")
    private BigDecimal consignedAmount;

    @Schema(description = "入库数量")
    private BigDecimal inboundAmount;

    @Schema(description = "入库人")
    private String inboundBy;

    @Schema(description = "出库时间")
    private LocalDateTime inboundTime;

    @Schema(description = "确认数量")
    private BigDecimal signedAmount;

    @Schema(description = "确认人")
    private String signedBy;

    @Schema(description = "确认日期")
    private LocalDateTime signedTime;

    @Schema(description = "物料库存ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "4284")
    @NotEmpty(message = "物料库存ID不能为空")
    private String materialStockId;

    @Schema(description = "物料条码", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotEmpty(message = "物料条码不能为空")
    private String barCode;

    @Schema(description = "物料批次号", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotEmpty(message = "物料批次号不能为空")
    private String batchNumber;

    @Schema(description = "收货单ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "9994")
    @ExcelProperty("收货单ID")
    private String consignmentId;

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

    /***
     * 收货单编号
     */
    private String consignmentNo;
    /***
     * 收货单名称
     */
    private String consignmentName;

    /***
     * 库存数量
     */
    private BigDecimal quantity;

    @Schema(description = "物料类型Id", requiredMode = Schema.RequiredMode.REQUIRED)
    private String materialId;



}