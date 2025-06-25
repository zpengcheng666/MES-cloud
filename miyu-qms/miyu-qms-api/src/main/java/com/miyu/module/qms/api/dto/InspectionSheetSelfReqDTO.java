package com.miyu.module.qms.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import javax.validation.constraints.NotEmpty;

@Schema(description = "RPC 服务 - 质量系统 检验单 Resquest DTO")
@Data
public class InspectionSheetSelfReqDTO {

    @Schema(description = "单号", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @NotEmpty(message = "单号不能为空")
    private String recordNumber;

    @Schema(description = "物料类型", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @NotEmpty(message = "物料类型不能为空")
    private String materialConfigId;

    @Schema(description = "物料批次号")
    @NotEmpty(message = "批次号不能为空")
    private String batchNumber;

    @Schema(description = "物料条码")
    @NotEmpty(message = "物料条码不能为空")
    private String barCode;

    @Schema(description = "工序ID")
    private String processId;

    @Schema(description = "产品ID")
    private String materialId;

    @Schema(description = "自检人员ID")
    @NotEmpty(message = "自检人员不能为空")
    private String assignmentId;

    @Schema(description = "检验方案ID")
    @NotEmpty(message = "检验方案不能为空")
    private String schemeId;

    @Schema(description = "检验单来源")
    @NotEmpty(message = "检验单来源不能为空")
    private Integer sourceType;

    /**
     *  1 需要首检
     */
    @Schema(description = "是否首检", example = "1")
    private Integer needFirstInspection;

}
