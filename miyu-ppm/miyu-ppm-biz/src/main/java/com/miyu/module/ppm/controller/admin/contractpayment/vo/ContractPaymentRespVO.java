package com.miyu.module.ppm.controller.admin.contractpayment.vo;

import cn.iocoder.yudao.framework.excel.core.annotations.DictFormat;
import cn.iocoder.yudao.framework.excel.core.convert.DictConvert;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import java.util.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import com.alibaba.excel.annotation.*;

import javax.validation.constraints.NotNull;

@Schema(description = "管理后台 - 合同付款 Response VO")
@Data
@ExcelIgnoreUnannotated
public class ContractPaymentRespVO {

    @Schema(description = "合同编号", example = "你猜")
    @ExcelProperty("合同编号")
    private String contractNumber;

    @Schema(description = "主键", requiredMode = Schema.RequiredMode.REQUIRED, example = "28123")
    private String id;

    @Schema(description = "合同ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "27607")
    private String contractId;

    @Schema(description = "税务信息ID", example = "27389")
    private String financeId;

    @Schema(description = "实际付款日期", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("实际付款日期")
    private LocalDateTime payDate;

    @Schema(description = "实际付款金额", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("实际付款金额")
    private BigDecimal amount;

    @Schema(description = "实际付款方式", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty(value = "实际付款方式", converter = DictConvert.class)
    @DictFormat("pd_finance_pay_method")
    private Integer method;

    @Schema(description = "付款凭证")
    private String evidence;

    @Schema(description = "审批状态")
    private Integer status;

    @Schema(description = "备注", example = "你猜")
    @ExcelProperty("备注")
    private String remark;

    @Schema(description = "工作流编号")
    private String processInstanceId;

    @Schema(description = "创建时间", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("创建时间")
    private LocalDateTime createTime;


    @Schema(description = "合同名称", example = "你猜")
    private String contractName;

    @Schema(description = "付款计划详细列表")
    private List<PaymentDetail> paymentDetails;

    @Schema(description = "付款计划详细列表")
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PaymentDetail {

        @Schema(description = "付款计划主键", requiredMode = Schema.RequiredMode.REQUIRED, example = "1234")
        @NotNull(message = "付款计划不能为空")
        private String schemeId;

        @Schema(description = "金额", requiredMode = Schema.RequiredMode.REQUIRED, example = "123.00")
        @NotNull(message = "金额不能为空")
        private BigDecimal amount;

        @Schema(description = "结算方式")
        private Integer paymentControl;

        @Schema(description = "付款日期")
        private LocalDateTime payDate;

        @Schema(description = "比例")
        private BigDecimal ratio;

        @Schema(description = "金额")
        private BigDecimal schemeAmount;

        @Schema(description = "付款方式")
        private Integer method;

        @Schema(description = "备注")
        private String remark;

        @Schema(description = "剩余支付金额")
        private BigDecimal remainAmount;

//        @Schema(description = "已付金额")
//        private BigDecimal payAmount;
    }

}