package com.miyu.module.qms.controller.admin.inspectionsheet.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import javax.validation.constraints.*;
import java.time.LocalDateTime;

@Schema(description = "管理后台 - 检验单新增/修改 Request VO")
@Data
public class InspectionSheetSaveReqVO {

    @Schema(description = "主键", requiredMode = Schema.RequiredMode.REQUIRED, example = "14202")
    private String id;

    @Schema(description = "检验单名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "李四")
    private String sheetName;

    @Schema(description = "检验单号", requiredMode = Schema.RequiredMode.REQUIRED)
    private String sheetNo;

    @Schema(description = "来源单号", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotEmpty(message = "来源单号不能为空")
    private String recordNumber;

    @Schema(description = "工序任务ID")
    private String  recordId;

    @Schema(description = "质检状态  0待派工1待检验2检验中3检验完成", example = "1")
    private Integer status;

    @Schema(description = "负责人", requiredMode = Schema.RequiredMode.REQUIRED)
    private String header;

    @Schema(description = "开始时间")
    private LocalDateTime beginTime;

    @Schema(description = "结束时间")
    private LocalDateTime endTime;

    @Schema(description = "物料类型", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @NotEmpty(message = "物料类型不能为空")
    private String materialConfigId;

    @Schema(description = "方案类型 来料检验  生产检验",  requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @NotNull(message = "方案类型不能为空")
    private Integer schemeType;

    @Schema(description = "是否首检", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    private Integer needFirstInspection;

    @Schema(description = "产品数量", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @NotNull(message = "产品数量不能为空")
    private Integer quantity;

    @Schema(description = "工艺ID")
    private String technologyId;

    @Schema(description = "工序ID")
    private String processId;

    @Schema(description = "检验类型")
    private Integer inspectionSheetType;

    @Schema(description = "物料批次号")
    private String batchNumber;

    @Schema(description = "检验水平类型")
    private Integer levelType;

    @Schema(description = "检验类型")
    private Integer ruleType;

    @Schema(description = "是否自检")
    private Integer isSelfInspection;

    @Schema(description = "物料条码")
    private String barCode;

    @Schema(description = "产品ID")
    private String materialId;

    @Schema(description = "检验方案ID")
    @NotEmpty(message = "检验方案不能为空")
    private String schemeId;

    @Schema(description = "自检人员ID")
    private String assignmentId;

    @NotNull(message = "检验单来源不能为空")
    private Integer sourceType;

    @Schema(description = "检验单任务主键")
    private String taskId;
}
