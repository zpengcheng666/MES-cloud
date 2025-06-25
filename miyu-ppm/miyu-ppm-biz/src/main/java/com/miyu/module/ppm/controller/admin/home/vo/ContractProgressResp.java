package com.miyu.module.ppm.controller.admin.home.vo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Schema(description = "管理后台 - 采购合同执行情况 Response VO")
@Data
@ExcelIgnoreUnannotated
public class ContractProgressResp {


    @Schema(description = "订单ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "15782")
    private String id;

    @Schema(description = "合同ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "18620")
    private String contractId;

    @Schema(description = "合同编号", requiredMode = Schema.RequiredMode.REQUIRED)
    private String number;

    @Schema(description = "合同名称", example = "李四")
    private String name;

    @Schema(description = "合同订单数量", requiredMode = Schema.RequiredMode.REQUIRED)
    private BigDecimal quantity;

    @Schema(description = "交货日期", requiredMode = Schema.RequiredMode.REQUIRED)
    private LocalDateTime leadDate;

    @Schema(description = "交付数量", requiredMode = Schema.RequiredMode.REQUIRED)
    private BigDecimal finishQuantity;

    /***
     * >10天内正常
     * 0-10天 临期
     *<0逾期
     * 交付数量==订单数量  完成
     */
    @Schema(description = "交付状态 1逾期 2临期 3正常 4 交付完成", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer  leadStatus;
    private String  materialName;
    private String  materialNumber;
}



