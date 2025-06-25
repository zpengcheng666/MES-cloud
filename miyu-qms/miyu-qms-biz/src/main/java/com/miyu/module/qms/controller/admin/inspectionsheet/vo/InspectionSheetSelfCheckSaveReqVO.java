package com.miyu.module.qms.controller.admin.inspectionsheet.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Schema(description = "管理后台 - 检验单新增/修改 Request VO")
@Data
public class InspectionSheetSelfCheckSaveReqVO {

    @Schema(description = "主键", requiredMode = Schema.RequiredMode.REQUIRED, example = "14202")
    private String id;

    @Schema(description = "检验单名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "李四")
    private String sheetName;

    @Schema(description = "检验单号", requiredMode = Schema.RequiredMode.REQUIRED)
    private String sheetNo;

    @Schema(description = "生产单号", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotEmpty(message = "生产不能为空")
    private String recordNumber;

    @Schema(description = "方案类型 生产检验",  requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @NotNull(message = "方案类型不能为空")
    private Integer schemeType;

    @Schema(description = "物料类型")
    private String materialConfigId;

    @Schema(description = "产品ID")
    @NotEmpty(message = "产品不能为空")
    private String materialId;

    @Schema(description = "物料批次号")
    private String batchNumber;

    @Schema(description = "工序ID")
    @NotEmpty(message = "工序ID不能为空")
    private String processId;

    @Schema(description = "检验方案ID")
    @NotEmpty(message = "检验方案不能为空")
    private String schemeId;

    @Schema(description = "是否自检")
    private Integer isSelfInspection;

    @Schema(description = "自检人员ID")
    private String assignmentId;

    @Schema(description = "物料条码")
    @NotEmpty(message = "物料条码不能为空")
    private String barCode;

    @Schema(description = "检验单来源")
    @NotNull(message = "检验单来源不能为空")
    private Integer sourceType;

}
