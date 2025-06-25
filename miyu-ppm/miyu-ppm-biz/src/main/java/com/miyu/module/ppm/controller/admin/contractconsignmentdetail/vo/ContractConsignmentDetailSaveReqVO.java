package com.miyu.module.ppm.controller.admin.contractconsignmentdetail.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import javax.validation.constraints.*;
import java.math.BigDecimal;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;

@Schema(description = "管理后台 - 外协发货单详情新增/修改 Request VO")
@Data
public class ContractConsignmentDetailSaveReqVO {

    @Schema(description = "ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "1381")
    private String id;

    @Schema(description = "发货单ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "3674")
    @NotEmpty(message = "发货单ID不能为空")
    private String consignmentId;

    @Schema(description = "发货数量", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "发货数量不能为空")
    private BigDecimal consignedAmount;

    @Schema(description = "出库数量")
    private BigDecimal inboundAmount;

    @Schema(description = "出库人")
    private String inboundBy;

    @Schema(description = "出库时间")
    private LocalDateTime inboundTime;

    @Schema(description = "确认数量")
    private BigDecimal signedAmount;

    @Schema(description = "确认人")
    private String signedBy;

    @Schema(description = "确认日期")
    private LocalDateTime signedTime;

    @Schema(description = "物料库存ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "29725")
    @NotEmpty(message = "物料库存ID不能为空")
    private String materialStockId;

    @Schema(description = "物料条码", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotEmpty(message = "物料条码不能为空")
    private String barCode;

    @Schema(description = "物料批次号", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotEmpty(message = "物料批次号不能为空")
    private String batchNumber;

    @Schema(description = "物料类型ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "10262")
    @NotEmpty(message = "物料类型ID不能为空")
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