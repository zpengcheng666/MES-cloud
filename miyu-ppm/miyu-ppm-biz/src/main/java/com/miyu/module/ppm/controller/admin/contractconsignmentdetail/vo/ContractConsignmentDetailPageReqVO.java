package com.miyu.module.ppm.controller.admin.contractconsignmentdetail.vo;

import lombok.*;
import java.util.*;
import io.swagger.v3.oas.annotations.media.Schema;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import java.math.BigDecimal;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;

import static cn.iocoder.yudao.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

@Schema(description = "管理后台 - 外协发货单详情分页 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class ContractConsignmentDetailPageReqVO extends PageParam {

    @Schema(description = "发货单ID", example = "3674")
    private String consignmentId;

    @Schema(description = "发货数量")
    private BigDecimal consignedAmount;

    @Schema(description = "出库数量")
    private BigDecimal inboundAmount;

    @Schema(description = "出库人")
    private String inboundBy;

    @Schema(description = "出库时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] inboundTime;

    @Schema(description = "确认数量")
    private BigDecimal signedAmount;

    @Schema(description = "确认人")
    private String signedBy;

    @Schema(description = "确认日期")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] signedTime;

    @Schema(description = "创建时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] createTime;

    @Schema(description = "物料库存ID", example = "29725")
    private String materialStockId;

    @Schema(description = "物料条码")
    private String barCode;

    @Schema(description = "物料批次号")
    private String batchNumber;

    @Schema(description = "物料类型ID", example = "10262")
    private String materialConfigId;

    @Schema(description = "项目ID", example = "4872")
    private String projectId;

    @Schema(description = "项目订单ID", example = "5156")
    private String projectOrderId;

    @Schema(description = "项目子计划ID", example = "4375")
    private String projectPlanId;

    @Schema(description = "合同订单ID", example = "19499")
    private String orderId;

}