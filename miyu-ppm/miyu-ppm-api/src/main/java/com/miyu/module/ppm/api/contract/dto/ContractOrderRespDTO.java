package com.miyu.module.ppm.api.contract.dto;


import cn.hutool.core.date.DateTime;
import com.fhs.core.trans.vo.VO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Schema(description = "RPC 服务 - 采购系统 合同订单 Response DTO")
@Data
public class ContractOrderRespDTO implements VO {
    @Schema(description = "ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "25351")
    private String id;

    @Schema(description = "合同ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    private String contractId;

    @Schema(description = "物料类型Id", requiredMode = Schema.RequiredMode.REQUIRED)
    private String materialId;

    @Schema(description = "数量", example = "1")
    private BigDecimal quantity;

    @Schema(description = "价格", requiredMode = Schema.RequiredMode.REQUIRED)
    private BigDecimal price;

    @Schema(description = "税率", requiredMode = Schema.RequiredMode.REQUIRED)
    private BigDecimal taxRate;

    @Schema(description = "含税单价", requiredMode = Schema.RequiredMode.REQUIRED)
    private BigDecimal taxPrice;

    @Schema(description = "交货日期", requiredMode = Schema.RequiredMode.REQUIRED)
    private LocalDateTime leadDate;


}