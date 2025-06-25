package com.miyu.module.ppm.controller.admin.contractrefund.vo;

import com.miyu.module.ppm.enums.contractrefund.ContractRefundEnum;
import com.miyu.module.ppm.enums.shipping.DMAuditStatusEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.util.*;
import java.util.*;
import java.math.BigDecimal;

import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

import com.alibaba.excel.annotation.*;
import cn.iocoder.yudao.framework.excel.core.annotations.DictFormat;
import cn.iocoder.yudao.framework.excel.core.convert.DictConvert;

@Schema(description = "管理后台 - 合同退款 Response VO")
@Data
@ExcelIgnoreUnannotated
public class ContractRefundRespVO {

    @Schema(description = "主键", requiredMode = Schema.RequiredMode.REQUIRED, example = "15419")
    @ExcelProperty("主键")
    private String id;
    @Schema(description = "退款单号", example = "32569")
    @ExcelProperty("退款单号")
    private String no;

    @Schema(description = "退货单", example = "32569")
    @ExcelProperty("退货单")
    private String shippingReturnId;
    @Schema(description = "退货单号", example = "32569")
    @ExcelProperty("退货单号")
    private String shippingReturnNo;

    @Schema(description = "合同ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "16435")
    @ExcelProperty("合同ID")
    private String contractId;

    @Schema(description = "合同号", requiredMode = Schema.RequiredMode.REQUIRED, example = "16435")
    @ExcelProperty("合同号")
    private String contractNo;
    @Schema(description = "合同名", requiredMode = Schema.RequiredMode.REQUIRED, example = "16435")
    @ExcelProperty("合同名")
    private String contractName;

    @Schema(description = "合作方", requiredMode = Schema.RequiredMode.REQUIRED, example = "16435")
    @ExcelProperty("合作方")
    private String companyName;

    @Schema(description = "退款方式现金、承兑、信用证、支付宝、二维码等", example = "1")
    @ExcelProperty(value = "退款方式现金、承兑、信用证、支付宝、二维码等", converter = DictConvert.class)
    @DictFormat("pd_finance_pay_method") // TODO 代码优化：建议设置到对应的 DictTypeConstants 枚举类中
    private Integer refundType;

    @Schema(description = "退款日期")
    @ExcelProperty("退款日期")
    private LocalDateTime refundTime;

    @Schema(description = "退款金额", example = "22579")
    @ExcelProperty("退款金额")
    private BigDecimal refundPrice;

    @Schema(description = "创建时间", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("创建时间")
    private LocalDateTime createTime;



    /**
     * 工作流编号
     *
     * 关联 ProcessInstance 的 id 属性
     */

    @Schema(description = "流程ID", example = "32569")
    private String processInstanceId;
    /**
     * 审批状态
     *
     * 枚举 {@link DMAuditStatusEnum}
     */
    @Schema(description = "审批状态")
    private Integer status;


    /**
     * 状态  0已创建 1审批中 2退款中 3结束 8审核失败9作废
     *
     * 枚举 {@link ContractRefundEnum}
     */

    @Schema(description = "退款状态")
    private Integer refundStatus;

}