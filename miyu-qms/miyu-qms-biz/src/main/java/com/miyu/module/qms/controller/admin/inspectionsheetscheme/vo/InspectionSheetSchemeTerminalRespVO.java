package com.miyu.module.qms.controller.admin.inspectionsheetscheme.vo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema(description = "管理后台 - 检验单方案任务计划 Response VO")
@Data
@ExcelIgnoreUnannotated
public class InspectionSheetSchemeTerminalRespVO {

    @Schema(description = "主键")
    private String id;

    @Schema(description = "检验单ID")
    private String inspectionSheetId;

    @Schema(description = "总数量")
    private Integer quantity;

    @Schema(description = "待检测数量")
    private Integer toInspectionQuantity;

    @Schema(description = "已检测数量")
    private Integer inspectedQuantity;

    @Schema(description = "合格数量")
    private Integer qualifiedQuantity;

    @Schema(description = "不合格数量")
    private Integer unqualifiedQuantity;

    @Schema(description = "检验单号")
    private String recordNumber;

    @Schema(description = "是否合格")
    private Integer inspectionResult;

    @Schema(description = "是否自检")
    private Integer selfInspection;

    @Schema(description = "自检单状态")
    private Integer selfAssignmentStatus;

    @Schema(description = "物料条码")
    private String barCode;

    @Schema(description = "方案类型")
    private Integer schemeType;

    @Schema(description = "检验单来源")
    private Integer sourceType;
}
