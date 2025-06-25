package com.miyu.module.ppm.controller.admin.consignmentrefund.vo;

import lombok.*;
import java.util.*;
import io.swagger.v3.oas.annotations.media.Schema;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import java.math.BigDecimal;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;

import static cn.iocoder.yudao.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

@Schema(description = "管理后台 - 采购退款单分页 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class ConsignmentRefundPageReqVO extends PageParam {

    @Schema(description = "采购退款单号")
    private String no;

    @Schema(description = "采购退货单", example = "15536")
    private String consignmentReturnId;

    @Schema(description = "合同ID", example = "19790")
    private String contractId;

    @Schema(description = "退款方式现金、承兑、信用证、支付宝、二维码等", example = "1")
    private Integer refundType;

    @Schema(description = "退款日期")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] refundTime;

    @Schema(description = "退款金额", example = "4907")
    private BigDecimal refundPrice;

    @Schema(description = "创建时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] createTime;

    @Schema(description = "审批状态", example = "2")
    private Integer status;

    @Schema(description = "工作流编号", example = "16867")
    private String processInstanceId;

    @Schema(description = "状态  0已创建 1审批中 2退款中 3结束 8审核失败 9作废", example = "1")
    private Integer refundStatus;

}