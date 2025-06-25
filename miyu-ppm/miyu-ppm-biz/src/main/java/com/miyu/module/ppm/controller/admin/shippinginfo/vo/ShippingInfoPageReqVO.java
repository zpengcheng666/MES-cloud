package com.miyu.module.ppm.controller.admin.shippinginfo.vo;

import lombok.*;
import java.util.*;
import io.swagger.v3.oas.annotations.media.Schema;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import java.math.BigDecimal;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;

import static cn.iocoder.yudao.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

@Schema(description = "管理后台 - 销售发货产品分页 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class ShippingInfoPageReqVO extends PageParam {

    @Schema(description = "发货单ID", example = "17916")
    private String shippingId;

    @Schema(description = "合同ID", example = "23817")
    private String projectId;

    @Schema(description = "合同订单ID", example = "12832")
    private String orderId;

    @Schema(description = "合同ID", example = "10988")
    private String contractId;

    @Schema(description = "发货数量")
    private BigDecimal consignedAmount;

    @Schema(description = "出库数量")
    private Long outboundAmount;

    @Schema(description = "出库人")
    private String outboundBy;

    @Schema(description = "出库时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] outboundTime;

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

    @Schema(description = "物料类型", example = "16657")
    private String materialConfigId;

    @Schema(description = "状态  0已创建 1审批中 2 待出库 3出库中4发货确认5结束 9作废8审批失败", example = "2")
    private Integer status;

}