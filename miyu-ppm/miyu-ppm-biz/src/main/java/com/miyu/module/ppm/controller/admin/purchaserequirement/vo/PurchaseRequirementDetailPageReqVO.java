package com.miyu.module.ppm.controller.admin.purchaserequirement.vo;

import lombok.*;
import io.swagger.v3.oas.annotations.media.Schema;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import java.math.BigDecimal;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;

import static cn.iocoder.yudao.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

@Schema(description = "管理后台 - 采购需求明细，可以来源于采购申请或MRP分页 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class PurchaseRequirementDetailPageReqVO extends PageParam {

    @Schema(description = "申请单ID", example = "17326")
    private String requirementId;

    @Schema(description = "源单类型", example = "1")
    private Integer sourceType;

    @Schema(description = "源单id", example = "22119")
    private String sourceId;

    @Schema(description = "需求物料")
    private String requiredMaterial;

    @Schema(description = "需求数量")
    private BigDecimal requiredQuantity;

    @Schema(description = "需求时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] requiredDate;

    @Schema(description = "预算单价", example = "6721")
    private BigDecimal estimatedPrice;

    @Schema(description = "供应商，即企业ID")
    private String supplier;

    @Schema(description = "创建时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] createTime;

    @Schema(description = "创建IP")
    private String creationIp;

    @Schema(description = "更新ip")
    private String updatedIp;

}