package com.miyu.module.qms.controller.admin.inspectionsheetgeneratetaskdetail.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Schema(description = "管理后台 - 检验单生成任务明细新增/修改 Request VO")
@Data
public class InspectionSheetGenerateTaskDetailSaveReqVO {

    @Schema(description = "主键", requiredMode = Schema.RequiredMode.REQUIRED, example = "18519")
    private String id;

    @Schema(description = "检验单任务ID", example = "2297")
    private String taskId;

    @Schema(description = "物料ID", example = "14286")
    private String materialId;

    @Schema(description = "物料类型ID", example = "12577")
    private String materialConfigId;

    @Schema(description = "物料条码")
    private String barCode;

    @Schema(description = "物料批次号")
    private String batchNumber;

    @Schema(description = "状态 0 未生成 1 已生成", example = "2")
    private Integer status;

}
