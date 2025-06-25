package com.miyu.module.ppm.controller.admin.consignmentrefund.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import javax.validation.constraints.*;
import java.math.BigDecimal;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;

@Schema(description = "管理后台 - 采购退款单新增/修改 Request VO")
@Data
public class ConsignmentRefundSaveReqVO {

    @Schema(description = "采购退款单主键", requiredMode = Schema.RequiredMode.REQUIRED, example = "24097")
    private String id;

    @Schema(description = "采购退款单号", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotEmpty(message = "采购退款单号不能为空")
    private String no;

    @Schema(description = "采购退货单", example = "15536")
    private String consignmentReturnId;

    @Schema(description = "合同ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "19790")
    @NotEmpty(message = "合同ID不能为空")
    private String contractId;

    @Schema(description = "退款方式现金、承兑、信用证、支付宝、二维码等", example = "1")
    private Integer refundType;

    @Schema(description = "退款日期")
    private LocalDateTime refundTime;

    @Schema(description = "退款金额", example = "4907")
    private BigDecimal refundPrice;

    @Schema(description = "审批状态", requiredMode = Schema.RequiredMode.REQUIRED, example = "2")
    private Integer status;

    @Schema(description = "工作流编号", example = "16867")
    private String processInstanceId;

    @Schema(description = "状态  0已创建 1审批中 2退款中 3结束 8审核失败 9作废", example = "1")
    private Integer refundStatus;

}