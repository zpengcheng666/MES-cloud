package com.miyu.module.ppm.controller.admin.shippingdetail.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import javax.validation.constraints.*;
import java.math.BigDecimal;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;

@Schema(description = "管理后台 - 销售发货明细新增/修改 Request VO")
@Data
public class ShippingDetailSaveReqVO {

    @Schema(description = "ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "27513")
    private String id;

    @Schema(description = "发货单ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "8204")
    @NotEmpty(message = "发货单ID不能为空")
    private String shippingId;

    @Schema(description = "合同订单ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "2595")
    @NotEmpty(message = "合同订单ID不能为空")
    private String orderId;

    @Schema(description = "发货数量", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "发货数量不能为空")
    private BigDecimal consignedAmount;

    @Schema(description = "确认数量")
    private BigDecimal signedAmount;

    @Schema(description = "确认")
    private String signedBy;

    @Schema(description = "确认日期")
    private LocalDateTime signedTime;

}