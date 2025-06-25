package com.miyu.module.ppm.controller.admin.shippingdetail.vo;

import lombok.*;
import java.util.*;
import io.swagger.v3.oas.annotations.media.Schema;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import java.math.BigDecimal;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;

import static cn.iocoder.yudao.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

@Schema(description = "管理后台 - 销售发货明细分页 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class ShippingDetailPageReqVO extends PageParam {

    @Schema(description = "发货单ID", example = "8204")
    private String shippingId;

    @Schema(description = "合同ID", example = "8204")
    private String contractId;

    @Schema(description = "合同订单ID", example = "2595")
    private String orderId;

    @Schema(description = "发货数量")
    private BigDecimal consignedAmount;

    @Schema(description = "确认数量")
    private BigDecimal signedAmount;

    @Schema(description = "确认")
    private String signedBy;

    @Schema(description = "确认日期")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] signedTime;

}