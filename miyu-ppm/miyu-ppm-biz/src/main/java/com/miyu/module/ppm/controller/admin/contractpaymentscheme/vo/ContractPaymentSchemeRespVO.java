package com.miyu.module.ppm.controller.admin.contractpaymentscheme.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import com.alibaba.excel.annotation.*;

@Schema(description = "管理后台 - 合同付款计划 Response VO")
@Data
@ExcelIgnoreUnannotated
public class ContractPaymentSchemeRespVO {

    @Schema(description = "主键", requiredMode = Schema.RequiredMode.REQUIRED, example = "14395")
    @ExcelProperty("主键")
    private String id;

    @Schema(description = "合同ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "23198")
    @ExcelProperty("合同ID")
    private String contractId;

    @Schema(description = "计划编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "23198")
    @ExcelProperty("计划编号")
    private String number;

    @Schema(description = "付款控制，货到付款、预付、货到票据支付、货到分期、货到延期等", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("付款控制，货到付款、预付、货到票据支付、货到分期、货到延期等")
    private Integer paymentControl;

    @Schema(description = "付款日期", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("付款日期")
    private LocalDateTime payDate;

    @Schema(description = "比例", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("比例")
    private BigDecimal ratio;

    @Schema(description = "金额，与比例联动", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("金额，与比例联动")
    private BigDecimal amount;

    @Schema(description = "付款方式，银行汇款、银行本票、商业汇票、现金、托收承付等", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("付款方式，银行汇款、银行本票、商业汇票、现金、托收承付等")
    private Integer method;

    @Schema(description = "备注", example = "随便")
    @ExcelProperty("备注")
    private String remark;

    @Schema(description = "创建时间", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("创建时间")
    private LocalDateTime createTime;

    @Schema(description = "剩余付款金额")
    private BigDecimal remainAmount;

//    @Schema(description = "已付款金额合计")
//    private BigDecimal payAmountSum;
}