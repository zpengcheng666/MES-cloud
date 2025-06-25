package com.miyu.module.ppm.controller.admin.home.vo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Schema(description = "管理后台 - 采购合同执行情况 Response VO")
@Data
@ExcelIgnoreUnannotated
public class PurchaseContractExecutionResp {


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


    /***
     * 项目ID
     */
    private String projectId;
    /***
     * 订单ID
     */
    private String orderId;
    /***
     * 项目计划ID
     */
    private String projectPlanId;
    /***
     * 项目子计划ID
     */
    private String projectPlanItemId;

    /**
     * 产品ID
     */
    private String materialId;


    @Schema(description = "正在加工数量")
    private Integer processed;

    @Schema(description = "未加工数量")
    private Integer unprocessed;

    @Schema(description = "已完成数量")
    private Integer completed;


    private BigDecimal rate;
}



