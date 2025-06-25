package com.miyu.module.ppm.controller.admin.shippingreturndetail.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import javax.validation.constraints.*;
import java.math.BigDecimal;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;

@Schema(description = "管理后台 - 销售退货单详情新增/修改 Request VO")
@Data
public class ShippingReturnDetailSaveReqVO {

    @Schema(description = "ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "4124")
    private String id;

    @Schema(description = "退货单ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "17113")
    @NotEmpty(message = "退货单ID不能为空")
    private String shippingReturnId;

    @Schema(description = "合同订单ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "16163")
    @NotEmpty(message = "合同订单ID不能为空")
    private String orderId;

    @Schema(description = "退货数量", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "退货数量不能为空")
    private BigDecimal consignedAmount;

    @Schema(description = "入库数量")
    private Long inboundAmount;

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

    @Schema(description = "物料库存ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "6074")
    @NotEmpty(message = "物料库存ID不能为空")
    private String materialStockId;

    @Schema(description = "物料条码", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotEmpty(message = "物料条码不能为空")
    private String barCode;

    @Schema(description = "物料批次号", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotEmpty(message = "物料批次号不能为空")
    private String batchNumber;

}