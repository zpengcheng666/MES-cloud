package com.miyu.module.ppm.controller.admin.shippinginstoragedetail.vo;

import lombok.*;
import java.util.*;
import io.swagger.v3.oas.annotations.media.Schema;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import java.math.BigDecimal;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;

import static cn.iocoder.yudao.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

@Schema(description = "管理后台 - 销售订单入库明细分页 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class ShippingInstorageDetailPageReqVO extends PageParam {

    @Schema(description = "收货单ID", example = "10542")
    private String shippingStorageId;

    @Schema(description = "项目ID", example = "18333")
    private String projectId;

    @Schema(description = "项目订单ID", example = "6295")
    private String orderId;

    @Schema(description = "创建时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] createTime;

    @Schema(description = "发货数量")
    private BigDecimal consignedAmount;

    @Schema(description = "签收数量")
    private BigDecimal signedAmount;

    @Schema(description = "签收人")
    private String signedBy;

    @Schema(description = "签收日期")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] signedTime;

}