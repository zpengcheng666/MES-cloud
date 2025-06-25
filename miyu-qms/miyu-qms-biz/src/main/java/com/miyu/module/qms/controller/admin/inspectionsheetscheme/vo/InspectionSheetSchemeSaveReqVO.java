package com.miyu.module.qms.controller.admin.inspectionsheetscheme.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import javax.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Schema(description = "管理后台 - 检验单方案任务计划新增/修改 Request VO")
@Data
public class InspectionSheetSchemeSaveReqVO {

    @Schema(description = "主键", requiredMode = Schema.RequiredMode.REQUIRED, example = "26486")
    private String id;

    @Schema(description = "检验单Id", example = "30999")
    private String inspectionSheetId;

    @Schema(description = "分配类型 1人员 2班组", example = "1")
    private Integer assignmentType;

    @Schema(description = "分配的检验人员", example = "4880")
    private String assignmentId;

    @Schema(description = "分配日期")
    private LocalDateTime assignmentDate;

    @Schema(description = "质检状态  0待派工1待检验2检验中3检验完成", example = "1")
    private Integer status;

    @Schema(description = "方案类型 来料检验  生产检验", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @NotNull(message = "方案类型 来料检验  生产检验不能为空")
    private Integer schemeType;

    @Schema(description = "检验类型1抽检2全检", example = "1")
    private Integer inspectionSheetType;

    @Schema(description = "是否首检")
    private Integer needFirstInspection;

    @Schema(description = "物料类型ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "32568")
    @NotEmpty(message = "物料类型ID不能为空")
    private String materialConfigId;

    @Schema(description = "工艺ID", example = "8843")
    private String technologyId;

    @Schema(description = "工序ID", example = "25350")
    private String processId;

    @Schema(description = "检验方案ID", example = "31934")
    private String inspectionSchemeId;

    @Schema(description = "通过准则")
    private String passRule;

    @Schema(description = "计划检验时间")
    private LocalDateTime planTime;

    @Schema(description = "实际开始时间")
    private LocalDateTime beginTime;

    @Schema(description = "实际结束时间")
    private LocalDateTime endTime;

    @Schema(description = "检测数量")
    private BigDecimal inspectionQuantity;

    @Schema(description = "合格数量")
    private BigDecimal qualifiedQuantity;

    @Schema(description = "检测结果 1合格 2不合格")
    private Integer inspectionResult;

}