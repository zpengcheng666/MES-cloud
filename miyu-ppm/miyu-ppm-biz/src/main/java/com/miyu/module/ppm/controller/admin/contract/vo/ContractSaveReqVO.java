package com.miyu.module.ppm.controller.admin.contract.vo;

import com.miyu.module.ppm.framework.operatelog.core.company.CompanySupplyTypeParseFunction;
import com.miyu.module.ppm.framework.operatelog.core.contract.*;
import com.mzt.logapi.starter.annotation.DiffLogField;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.util.*;
import java.time.LocalDateTime;
import javax.validation.Valid;
import javax.validation.constraints.*;

import static cn.iocoder.yudao.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY;


@Schema(description = "管理后台 - 购销合同新增/修改 Request VO")
@Data
public class ContractSaveReqVO {

    @Schema(description = "ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "25351")
    private String id;

    @Schema(description = "类型(采购、销售)", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @NotNull(message = "类型不能为空")
    private Integer type;

    @Schema(description = "合同编号", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotEmpty(message = "合同编号不能为空")
    @DiffLogField(name = "合同编号")
    private String number;

    @Schema(description = "合同名称", example = "李四")
    @DiffLogField(name = "合同名称")
    private String name;

    @Schema(description = "项目ID")
    @DiffLogField(name = "项目", function = ContractProjectParseFunction.NAME)
    private String projectId;


    @Schema(description = "合同方", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotEmpty(message = "合同方不能为空")
    @DiffLogField(name = "合同方", function = ContractCompanyParseFunction.NAME)
    private String party;

    @Schema(description = "签约人", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotEmpty(message = "签约人不能为空")
    @DiffLogField(name = "签约人", function = ContractContactParseFunction.NAME)
    private String contact;

    @Schema(description = "签约时间", requiredMode = Schema.RequiredMode.REQUIRED)
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY)
    @NotNull(message = "签约时间不能为空")
    @DiffLogField(name = "签约时间")
    private LocalDateTime signingDate;

    @Schema(description = "签约地点", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotEmpty(message = "签约地点不能为空")
    @DiffLogField(name = "签约地点")
    private String signingAddress;

    @Schema(description = "签约部门", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotEmpty(message = "签约部门不能为空")
    @DiffLogField(name = "签约部门", function = ContractDepartmentParseFunction.NAME)
    private String department;

    @Schema(description = "我方签约人", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotEmpty(message = "我方签约人不能为空")
    @DiffLogField(name = "我方签约人", function = ContractSelfPersonParseFunction.NAME)
    private String selfContact;

    @Schema(description = "是否增值税", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "是否增值税不能为空")
    @DiffLogField(name = "是否增值税", function = ContractVatParseFunction.NAME)
    private Integer vat;

    @Schema(description = "币种", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "币种不能为空")
    @DiffLogField(name = "币种", function = ContractCurrencyParseFunction.NAME)
    private Integer currency;

    @Schema(description = "交货方式", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "交货方式不能为空")
    @DiffLogField(name = "交货方式", function = ContractDeliveryParseFunction.NAME)
    private Integer delivery;

    @Schema(description = "合同状态", example = "1")
    private Integer contractStatus;

    @Schema(description = "审批状态", example = "1")
    private Integer status;

    @Schema(description = "采购员", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotEmpty(message = "采购员不能为空")
    @DiffLogField(name = "采购员", function = ContractSelfPersonParseFunction.NAME)
    private String purchaser;

    @Schema(description = "备注", example = "你猜")
    private String remark;

    @Schema(description = "创建IP")
    private String creationIp;

    @Schema(description = "更新ip")
    private String updatedIp;

    @Schema(description = "流程key")
    private String processKey;

    @Schema(description = "合同分类")
    @DiffLogField(name = "合同分类", function = ContractTypeParseFunction.NAME)
    private Integer contractType;

    @Schema(description = "产品列表")
    @Valid
    @NotEmpty(message = "合同订单不能为空")
    @DiffLogField(name = "合同订单", function = ContractProductParseFunction.NAME)
    private List<Product> products;

    @Schema(description = "产品列表")
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Product {

        @Schema(description = "采购申请明细ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "20529")
        private String requirementDetailId;

        @Schema(description = "产品名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "20529")
        @NotNull(message = "产品名称不能为空")
        private String materialId;

        @Schema(description = "产品数量", requiredMode = Schema.RequiredMode.REQUIRED, example = "123.000")
        @NotNull(message = "产品数量不能为空")
        private BigDecimal quantity;

        @Schema(description = "产品单价", requiredMode = Schema.RequiredMode.REQUIRED, example = "123.00")
        @NotNull(message = "产品单价不能为空")
        private BigDecimal price;

        @Schema(description = "税率", requiredMode = Schema.RequiredMode.REQUIRED, example = "123.00")
        @NotNull(message = "税率不能为空")
        private BigDecimal taxRate;

        @Schema(description = "含税单价", requiredMode = Schema.RequiredMode.REQUIRED, example = "123.00")
        @NotNull(message = "税率不能为空")
        private BigDecimal taxPrice;

        @Schema(description = "含税单价", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotNull(message = "税率不能为空")
        private LocalDateTime leadDate;

        @Schema(description = "项目ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "123.000")
        private String projectId;

        @Schema(description = "项目订单ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "123.000")
        private String orderId;

        @Schema(description = "项目计划ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "123.000")
        private String projectPlanId;
        @Schema(description = "项目子计划ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "123.000")
        private String projectPlanItemId;
    }

    @Schema(description = "付款计划列表")
    @Valid
    @NotEmpty(message = "付款计划不能为空")
    @DiffLogField(name = "付款计划", function = ContractPaymentParseFunction.NAME)
    private List<PaymentScheme> paymentSchemes;

    @Schema(description = "付款计划列表")
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PaymentScheme {

        @Schema(description = "结算方式", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
        @NotNull(message = "结算方式不能为空")
        private Integer paymentControl;

        @Schema(description = "付款日期", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotNull(message = "付款日期不能为空")
        private LocalDateTime payDate;

        @Schema(description = "比例", requiredMode = Schema.RequiredMode.REQUIRED, example = "123.00")
        @NotNull(message = "比例不能为空")
        private BigDecimal ratio;

        @Schema(description = "金额", requiredMode = Schema.RequiredMode.REQUIRED, example = "123.00")
        @NotNull(message = "金额不能为空")
        private BigDecimal amount;

        @Schema(description = "付款方式", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
        @NotNull(message = "付款方式不能为空")
        private Integer method;

        @Schema(description = "备注")
        private String remark;

    }
}
