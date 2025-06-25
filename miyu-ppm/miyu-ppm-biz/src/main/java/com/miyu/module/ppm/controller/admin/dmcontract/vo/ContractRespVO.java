package com.miyu.module.ppm.controller.admin.dmcontract.vo;

import cn.iocoder.yudao.framework.excel.core.annotations.DictFormat;
import cn.iocoder.yudao.framework.excel.core.convert.DictConvert;
import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import com.miyu.module.ppm.api.shipping.dto.ShippingDTO;
import com.miyu.module.ppm.api.shippingreturn.dto.ShippingReturnDTO;
import com.miyu.module.ppm.dal.dataobject.contractrefund.ContractRefundDO;
import com.miyu.module.ppm.api.contract.dto.ContractInvoiceDTO;
import com.miyu.module.ppm.api.contract.dto.ContractPaymentDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Schema(description = "管理后台 - 购销合同 Response VO")
@Data
@ExcelIgnoreUnannotated
public class ContractRespVO {

    @Schema(description = "ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "25351")
    private String id;

    @Schema(description = "类型(采购、销售)", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    private Integer type;

    @Schema(description = "合同编号", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("合同编号")
    private String number;

    @Schema(description = "合同名称", example = "李四")
    @ExcelProperty("合同名称")
    private String name;

    @Schema(description = "合同方", requiredMode = Schema.RequiredMode.REQUIRED)
    private String party;

    @Schema(description = "签约人", requiredMode = Schema.RequiredMode.REQUIRED)
    private String contact;

    @ExcelProperty("合同方")
    private String partyName;

    @ExcelProperty("签约人")
    private String contactName;

    @Schema(description = "签约时间", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("签约时间")
    private LocalDateTime signingDate;

    @Schema(description = "签约地点", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("签约地点")
    private String signingAddress;

    @Schema(description = "签约部门", requiredMode = Schema.RequiredMode.REQUIRED)
    private String department;

    @Schema(description = "我方签约人", requiredMode = Schema.RequiredMode.REQUIRED)
    private String selfContact;

    @ExcelProperty("签约部门")
    private String selfContactName;

    @ExcelProperty("我方签约人")
    private String departmentName;

    @Schema(description = "是否增值税", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty(value = "是否增值税", converter = DictConvert.class)
    @DictFormat("vat_type")
    private Integer vat;

    @Schema(description = "币种", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty(value = "币种", converter = DictConvert.class)
    @DictFormat("currency_type")
    private Integer currency;

    @Schema(description = "交货方式", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty(value = "交货方式", converter = DictConvert.class)
    @DictFormat("pd_contract_delivery")
    private Integer delivery;

    @Schema(description = "合同状态", example = "1")
    @ExcelProperty(value = "合同状态", converter = DictConvert.class)
    @DictFormat("pd_contract_status")
    private Integer contractStatus;

    @Schema(description = "审批状态", example = "1")
    private Integer status;

    @Schema(description = "采购员", requiredMode = Schema.RequiredMode.REQUIRED)
    private String purchaser;

    @ExcelProperty("采购员")
    private String purchaserName;

    @Schema(description = "备注", example = "你猜")
    @ExcelProperty("备注")
    private String remark;

    @Schema(description = "创建时间", requiredMode = Schema.RequiredMode.REQUIRED)
    private LocalDateTime createTime;

    @Schema(description = "创建IP")
    private String creationIp;

    @Schema(description = "更新ip")
    private String updatedIp;



    private String processInstanceId;

    @Schema(description = "产品列表")
    private List<Product> products;

    @Schema(description = "产品列表")
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Product {

        @Schema(description = "产品名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "20529")
        private String materialId;
        private String materialName;

        @Schema(description = "产品数量", requiredMode = Schema.RequiredMode.REQUIRED, example = "123.000")
        private BigDecimal quantity;

        @Schema(description = "产品单价", requiredMode = Schema.RequiredMode.REQUIRED, example = "123.00")
        private BigDecimal price;

        @Schema(description = "税率", requiredMode = Schema.RequiredMode.REQUIRED, example = "123.00")
        private BigDecimal taxRate;

        @Schema(description = "含税单价", requiredMode = Schema.RequiredMode.REQUIRED, example = "123.00")
        private BigDecimal taxPrice;

        @Schema(description = "交货周期", requiredMode = Schema.RequiredMode.REQUIRED)
        private LocalDateTime leadDate;

        @Schema(description = "产品单位", requiredMode = Schema.RequiredMode.REQUIRED, example = "李四")
        private String materialUnit;

        private String productId;

        private String productName;

        private String avgPrice;

        private String maxPrice;

        private String minPrice;

        private String latestPrice;

        private String initTax;



        private String projectId;
        /***
         * 订单ID
         */
        private String orderId;
        /***
         * 项目子计划ID
         */
        private String projectPlanId;
    }

    @Schema(description = "付款计划列表")
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

    @Schema(description = "合同支付")
    private List<ContractPaymentDTO> payments;

    @Schema(description = "合同发票")
    private List<ContractInvoiceDTO> invoices;

    @Schema(description = "发货单集合")
    private List<ShippingDTO> shippings;

    @Schema(description = "退货单集合")
    private List<ShippingReturnDTO> shippingReturns;

    @Schema(description = "退款集合")
    private List<ContractRefundDO> contractRefunds;

}