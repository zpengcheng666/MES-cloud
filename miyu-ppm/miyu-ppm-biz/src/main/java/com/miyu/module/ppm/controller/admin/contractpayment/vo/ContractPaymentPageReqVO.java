package com.miyu.module.ppm.controller.admin.contractpayment.vo;

import lombok.*;
import java.util.*;
import io.swagger.v3.oas.annotations.media.Schema;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import java.math.BigDecimal;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;

import static cn.iocoder.yudao.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

@Schema(description = "管理后台 - 合同付款分页 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class ContractPaymentPageReqVO extends PageParam {

    @Schema(description = "合同ID", example = "27607")
    private String contractId;

    @Schema(description = "合同编号", example = "27607")
    private String contractNumber;


    @Schema(description = "业务类型1采购 2销售", example = "1")
    private Integer businessType;

    @Schema(description = "实际付款日期")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] payDate;

    @Schema(description = "实际付款金额")
    private BigDecimal amount;

    @Schema(description = "实际付款方式")
    private Integer method;

    @Schema(description = "付款凭证")
    private String evidence;

    @Schema(description = "备注", example = "你猜")
    private String remark;

    @Schema(description = "创建时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] createTime;

}