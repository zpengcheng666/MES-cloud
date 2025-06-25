package com.miyu.module.ppm.controller.admin.shippinginfo.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import javax.validation.constraints.*;
import java.math.BigDecimal;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;

@Schema(description = "管理后台 - 销售发货产品新增/修改 Request VO")
@Data
public class ShippingInfoSaveReqVO {

    @Schema(description = "ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "27144")
    private String id;

    @Schema(description = "发货单ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "17916")
    @NotEmpty(message = "发货单ID不能为空")
    private String shippingId;

    @Schema(description = "合同ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "23817")
    @NotEmpty(message = "合同ID不能为空")
    private String projectId;

    @Schema(description = "合同订单ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "12832")
    @NotEmpty(message = "合同订单ID不能为空")
    private String orderId;

    @Schema(description = "合同ID", example = "10988")
    private String contractId;

    @Schema(description = "发货数量", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "发货数量不能为空")
    private BigDecimal consignedAmount;

    @Schema(description = "出库数量")
    private Long outboundAmount;

    @Schema(description = "出库人")
    private String outboundBy;

    @Schema(description = "出库时间")
    private LocalDateTime outboundTime;

    @Schema(description = "确认数量")
    private BigDecimal signedAmount;

    @Schema(description = "确认人")
    private String signedBy;

    @Schema(description = "确认日期")
    private LocalDateTime signedTime;

    @Schema(description = "物料类型", example = "16657")
    private String materialConfigId;

    @Schema(description = "状态  0已创建 1审批中 2 待出库 3出库中 4发货确认 5结束 9作废8审批失败", example = "2")
    private Integer status;

    @Schema(description = "发货单类型1销售发货2外协发货3采购退货4委托加工退货", example = "2")
    private Integer shippingType;

}