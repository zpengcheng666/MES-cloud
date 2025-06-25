package com.miyu.module.ppm.controller.admin.contractinvoice.vo;

import lombok.*;
import java.util.*;
import io.swagger.v3.oas.annotations.media.Schema;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import java.math.BigDecimal;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;

import static cn.iocoder.yudao.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

@Schema(description = "管理后台 - 购销合同发票分页 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class ContractInvoicePageReqVO extends PageParam {

    @Schema(description = "合同ID", example = "2818")
    private String contractId;

    @Schema(description = "合同编号", example = "27607")
    private String contractNumber;

    @Schema(description = "业务类型1采购 2销售", example = "1")
    private Integer businessType;

    @Schema(description = "类型，普票、专票、收据等", example = "2")
    private Integer type;

    @Schema(description = "金额")
    private BigDecimal amount;

    @Schema(description = "开具时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] invoiceDate;

    @Schema(description = "票据代码")
    private String invoiceNumber;

    @Schema(description = "票据代码2")
    private String invoiceNumber2;

    @Schema(description = "创建时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] createTime;

}