package com.miyu.module.ppm.controller.admin.consignmentreturndetail.vo;

import lombok.*;
import java.util.*;
import io.swagger.v3.oas.annotations.media.Schema;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import java.math.BigDecimal;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;

import static cn.iocoder.yudao.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

@Schema(description = "管理后台 - 销售退货单详情分页 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class ConsignmentReturnDetailPageReqVO extends PageParam {

    @Schema(description = "退货单ID", example = "3674")
    private String consignmentReturnId;

    @Schema(description = "发货单详情ID", example = "26224")
    private String consignmentDetailId;

    @Schema(description = "合同订单ID", example = "27093")
    private String orderId;

    @Schema(description = "退货数量")
    private BigDecimal consignedAmount;

    @Schema(description = "入库数量")
    private BigDecimal inboundAmount;

    @Schema(description = "入库人")
    private String inboundBy;

    @Schema(description = "出库时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] inboundTime;

    @Schema(description = "确认数量")
    private BigDecimal signedAmount;

    @Schema(description = "确认人")
    private String signedBy;

    @Schema(description = "确认日期")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] signedTime;

    @Schema(description = "创建时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] createTime;

    @Schema(description = "物料库存ID", example = "4284")
    private String materialStockId;

    @Schema(description = "物料条码")
    private String barCode;

    @Schema(description = "物料批次号")
    private String batchNumber;

}