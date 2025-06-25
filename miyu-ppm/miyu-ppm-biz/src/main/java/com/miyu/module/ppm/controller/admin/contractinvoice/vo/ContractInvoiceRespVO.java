package com.miyu.module.ppm.controller.admin.contractinvoice.vo;

import cn.iocoder.yudao.framework.excel.core.annotations.DictFormat;
import cn.iocoder.yudao.framework.excel.core.convert.DictConvert;
import com.baomidou.mybatisplus.annotation.TableField;
import com.miyu.module.ppm.controller.admin.contract.vo.ContractRespVO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import java.util.*;
import java.math.BigDecimal;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;
import com.alibaba.excel.annotation.*;

import javax.validation.constraints.NotNull;

@Schema(description = "管理后台 - 购销合同发票 Response VO")
@Data
@ExcelIgnoreUnannotated
public class ContractInvoiceRespVO {

    @Schema(description = "主键", requiredMode = Schema.RequiredMode.REQUIRED, example = "23687")
    private String id;

    @Schema(description = "合同ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "2818")
    private String contractId;

    @Schema(description = "合同编号", example = "你猜")
    @ExcelProperty("合同编号")
    private String contractNumber;

    @Schema(description = "业务类型1采购 2销售", example = "1")
    private Integer businessType;

    @Schema(description = "类型，普票、专票、收据等", requiredMode = Schema.RequiredMode.REQUIRED, example = "2")
    @ExcelProperty(value = "类型", converter = DictConvert.class)
    @DictFormat("pd_contract_invoice_type")
    private Integer type;

    @Schema(description = "金额", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("金额")
    private BigDecimal amount;

    @Schema(description = "开具时间")
    @ExcelProperty("开具时间")
    private LocalDateTime invoiceDate;

    @Schema(description = "票据代码")
    @ExcelProperty("票据代码")
    private String invoiceNumber;

    @Schema(description = "票据代码2")
    @ExcelProperty("票据代码2")
    private String invoiceNumber2;

    @Schema(description = "附件地址", example = "https://www.iocoder.cn")
    private String fileUrl;

    @Schema(description = "审批状态")
    private Integer status;

    @Schema(description = "创建时间", requiredMode = Schema.RequiredMode.REQUIRED)
    private LocalDateTime createTime;

    @Schema(description = "工作流编号")
    private String processInstanceId;

    @Schema(description = "合同名称", example = "你猜")
    private String contractName;

    @Schema(description = "付款表")
    private List<Invoice> invoiceDetails;

    @Schema(description = "付款表")
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Invoice {

        @Schema(description = "支付主键")
        private String paymentId;

        @Schema(description = "实际付款日期")
        private LocalDateTime payDate;

        @Schema(description = "实际支付金额")
        private BigDecimal amount;

        @Schema(description = "实际付款方式")
        private Integer method;

        // 付款金额
        @Schema(description = "付款金额")
        private BigDecimal payAmount;

        @Schema(description = "剩余金额")
        private BigDecimal remainAmount;
    }
}