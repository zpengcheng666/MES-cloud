package com.miyu.module.ppm.controller.admin.contractrefund.vo;

import lombok.*;
import java.util.*;
import io.swagger.v3.oas.annotations.media.Schema;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import java.math.BigDecimal;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;

import static cn.iocoder.yudao.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

@Schema(description = "管理后台 - 合同退款分页 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class ContractRefundPageReqVO extends PageParam {

    @Schema(description = "退货单", example = "32569")
    private String shippingReturnId;

    @Schema(description = "合同ID", example = "16435")
    private String contractId;
    @Schema(description = "退款单号", example = "32569")
    private String no;

    @Schema(description = "退款方式现金、承兑、信用证、支付宝、二维码等", example = "1")
    private Integer refundType;

    @Schema(description = "退款状态", example = "1")
    private Integer refundStatus;

    @Schema(description = "退款日期")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] retundTime;

    @Schema(description = "退款金额", example = "22579")
    private BigDecimal refundPrice;

    @Schema(description = "创建时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] createTime;

}