package com.miyu.module.ppm.controller.admin.consignmentrefund.vo;

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

@Schema(description = "管理后台 - 采购退款单 Response VO")
@Data
@ExcelIgnoreUnannotated
public class ConsignmentRefundRespVO {

    @Schema(description = "采购退款单主键", requiredMode = Schema.RequiredMode.REQUIRED, example = "24097")
    @ExcelProperty("采购退款单主键")
    private String id;

    @Schema(description = "采购退款单号", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("采购退款单号")
    private String no;

    @Schema(description = "采购退货单", example = "15536")
    @ExcelProperty("采购退货单")
    private String consignmentReturnId;

    @Schema(description = "合同ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "19790")
    @ExcelProperty("合同ID")
    private String contractId;

    @Schema(description = "合同号", requiredMode = Schema.RequiredMode.REQUIRED, example = "16435")
    @ExcelProperty("合同号")
    private String contractNo;

    @Schema(description = "合同名", requiredMode = Schema.RequiredMode.REQUIRED, example = "16435")
    @ExcelProperty("合同名")
    private String contractName;


    @Schema(description = "退款方式现金、承兑、信用证、支付宝、二维码等", example = "1")
    @ExcelProperty("退款方式现金、承兑、信用证、支付宝、二维码等")
    private Integer refundType;

    @Schema(description = "退款日期")
    @ExcelProperty("退款日期")
    private LocalDateTime refundTime;

    @Schema(description = "退款金额", example = "4907")
    @ExcelProperty("退款金额")
    private BigDecimal refundPrice;

    @Schema(description = "创建时间", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("创建时间")
    private LocalDateTime createTime;

    @Schema(description = "审批状态", requiredMode = Schema.RequiredMode.REQUIRED, example = "2")
    @ExcelProperty("审批状态")
    private Integer status;

    @Schema(description = "工作流编号", example = "16867")
    @ExcelProperty("工作流编号")
    private String processInstanceId;

    @Schema(description = "状态  0已创建 1审批中 2退款中 3结束 8审核失败 9作废", example = "1")
    @ExcelProperty(value = "状态  0已创建 1审批中 2退款中 3结束 8审核失败 9作废", converter = DictConvert.class)
    @DictFormat("qms_is_effective") // TODO 代码优化：建议设置到对应的 DictTypeConstants 枚举类中
    private Integer refundStatus;

}