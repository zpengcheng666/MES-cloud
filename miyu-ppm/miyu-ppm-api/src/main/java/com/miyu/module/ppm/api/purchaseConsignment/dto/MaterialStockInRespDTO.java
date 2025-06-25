package com.miyu.module.ppm.api.purchaseConsignment.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

@Schema(description = "RPC 服务 - 获取物料库存")
@Data
public class MaterialStockInRespDTO {

    @Schema(description = "ID", requiredMode = Schema.RequiredMode.REQUIRED)
    private String id;

    @Schema(description = "创建时间", requiredMode = Schema.RequiredMode.REQUIRED)
    private LocalDateTime createTime;

    @Schema(description = "物料类型id", requiredMode = Schema.RequiredMode.REQUIRED)
    private String materialConfigId;

    @Schema(description = "物料管理模式，1：单件，2：批量", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer materialManage;

    @Schema(description = "物料编号", requiredMode = Schema.RequiredMode.REQUIRED)
    private String materialNumber;

    @Schema(description = "物料条码", requiredMode = Schema.RequiredMode.REQUIRED)
    private String barCode;

    @Schema(description = "物料批次号", requiredMode = Schema.RequiredMode.REQUIRED)
    private String batchNumber;

    @Schema(description = "储位id", requiredMode = Schema.RequiredMode.REQUIRED)
    private String storageId;

    @Schema(description = "库位id", requiredMode = Schema.RequiredMode.REQUIRED)
    private String locationId;

    @Schema(description = "总库存")
    private Integer totality;

    @Schema(description = "物料状态(1-待质检、2-合格、3-不合格)")
    private Integer materialStatus;

    @Schema(description = "收货单类型")
    private Integer consignmentType;
    @Schema(description = "入库单号")
    private String  no;
    @Schema(description = "入库单类型")
    private Integer  orderType;


    @Schema(description = "状态(0待入库 1入库中)")
    private Integer status;
}
