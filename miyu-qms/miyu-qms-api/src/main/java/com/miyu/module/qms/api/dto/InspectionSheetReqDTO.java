package com.miyu.module.qms.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Schema(description = "RPC 服务 - 质量系统 检验单 Resquest DTO")
@Data
public class InspectionSheetReqDTO {

    @Schema(description = "单号", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @NotEmpty(message = "单号不能为空")
    private String recordNumber;

    @Schema(description = "产品数量", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @NotNull(message = "产品数量不能为空")
    private Integer quantity;

    @Schema(description = "物料类型", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @NotEmpty(message = "物料类型不能为空")
    private String materialConfigId;

    @Schema(description = "物料批次号")
    @NotEmpty(message = "批次号不能为空")
    private String batchNumber;

    @Schema(description = "物料条码")
    @NotEmpty(message = "物料条码不能为空")
    private String barCode;

    @Schema(description = "方案类型 来料检验  生产检验 成品检验",  requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @NotNull(message = "方案类型不能为空")
    private Integer schemeType;

    @Schema(description = "工序ID")
    private String processId;

    @Schema(description = "是否首检")
    private Integer needFirstInspection;

    @Schema(description = "是否自检")
    private Integer isSelfInspection;

    @Schema(description = "产品ID")
    private String materialId;

    @Schema(description = "检验方案ID")
    @NotEmpty(message = "检验方案不能为空")
    private String schemeId;

    @Schema(description = "检验单来源")
    @NotEmpty(message = "检验单来源不能为空")
    private Integer sourceType;
}
