package com.miyu.module.ppm.controller.admin.contractpaymentscheme.vo;

import lombok.*;
import io.swagger.v3.oas.annotations.media.Schema;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import java.math.BigDecimal;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;

import static cn.iocoder.yudao.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

@Schema(description = "管理后台 - 合同付款计划分页 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class ContractPaymentSchemePageReqVO extends PageParam {

    @Schema(description = "合同ID", example = "23198")
    private String contractId;

    @Schema(description = "付款控制，货到付款、预付、货到票据支付、货到分期、货到延期等")
    private Integer paymentControl;

    @Schema(description = "付款日期")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] payDate;

    @Schema(description = "比例")
    private BigDecimal ratio;

    @Schema(description = "金额，与比例联动")
    private BigDecimal amount;

    @Schema(description = "付款方式，银行汇款、银行本票、商业汇票、现金、托收承付等")
    private Integer method;

    @Schema(description = "备注", example = "随便")
    private String remark;

    @Schema(description = "创建时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] createTime;

}