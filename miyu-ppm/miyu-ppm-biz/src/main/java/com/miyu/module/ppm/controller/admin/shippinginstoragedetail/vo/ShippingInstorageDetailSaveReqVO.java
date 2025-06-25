package com.miyu.module.ppm.controller.admin.shippinginstoragedetail.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import javax.validation.constraints.*;
import java.math.BigDecimal;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;

@Schema(description = "管理后台 - 销售订单入库明细新增/修改 Request VO")
@Data
public class ShippingInstorageDetailSaveReqVO {

    @Schema(description = "ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "28936")
    private String id;

    @Schema(description = "收货单ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "10542")
    @NotEmpty(message = "收货单ID不能为空")
    private String shippingStorageId;

    @Schema(description = "项目ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "18333")
    @NotEmpty(message = "项目ID不能为空")
    private String projectId;

    @Schema(description = "项目订单ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "6295")
    @NotEmpty(message = "项目订单ID不能为空")
    private String orderId;

    @Schema(description = "发货数量", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "发货数量不能为空")
    private BigDecimal consignedAmount;

    @Schema(description = "签收数量")
    private BigDecimal signedAmount;

    @Schema(description = "签收人")
    private String signedBy;

    @Schema(description = "签收日期")
    private LocalDateTime signedTime;

}