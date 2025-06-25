package cn.iocoder.yudao.module.pms.controller.admin.plan.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

@Schema(description = "获取物料库存")
@Data
public class PlanMaterialStock {

    @Schema(description = "ID", requiredMode = Schema.RequiredMode.REQUIRED)
    private String id;

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

}
