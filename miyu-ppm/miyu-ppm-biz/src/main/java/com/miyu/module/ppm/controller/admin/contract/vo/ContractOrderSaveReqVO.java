package com.miyu.module.ppm.controller.admin.contract.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalTime;
import javax.validation.constraints.*;
import java.math.BigDecimal;

@Schema(description = "管理后台 - 合同订单新增/修改 Request VO")
@Data
public class ContractOrderSaveReqVO {

    @Schema(description = "ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "15782")
    private String id;

    @Schema(description = "合同ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "18620")
    @NotEmpty(message = "合同ID不能为空")
    private String contractId;

    @Schema(description = "产品ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "20650")
    @NotEmpty(message = "产品ID不能为空")
    private String materialId;

    @Schema(description = "数量", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "数量不能为空")
    private BigDecimal quantity;

    @Schema(description = "单价", requiredMode = Schema.RequiredMode.REQUIRED, example = "19985")
    @NotNull(message = "单价不能为空")
    private BigDecimal price;

    @Schema(description = "税率", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "税率不能为空")
    private BigDecimal taxRate;

    @Schema(description = "含税单价", requiredMode = Schema.RequiredMode.REQUIRED, example = "6658")
    @NotNull(message = "含税单价不能为空")
    private BigDecimal taxPrice;

    @Schema(description = "交货日期", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "交货日期不能为空")
    private LocalTime leadDate;

    @Schema(description = "创建IP")
    private String creationIp;

    @Schema(description = "更新ip")
    private String updatedIp;

}