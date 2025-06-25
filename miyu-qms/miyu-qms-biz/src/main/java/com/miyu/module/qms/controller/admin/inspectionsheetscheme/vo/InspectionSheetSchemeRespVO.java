package com.miyu.module.qms.controller.admin.inspectionsheetscheme.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import com.alibaba.excel.annotation.*;

@Schema(description = "管理后台 - 检验单方案任务计划 Response VO")
@Data
@ExcelIgnoreUnannotated
public class InspectionSheetSchemeRespVO {

    @Schema(description = "主键", requiredMode = Schema.RequiredMode.REQUIRED, example = "26486")
    @ExcelProperty("主键")
    private String id;

    @Schema(description = "创建时间", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("创建时间")
    private LocalDateTime createTime;

    @Schema(description = "检验单Id", example = "30999")
    @ExcelProperty("检验单Id")
    private String inspectionSheetId;

    @Schema(description = "分配类型 1人员 2班组", example = "1")
    @ExcelProperty("分配类型 1人员 2班组")
    private Integer assignmentType;

    @Schema(description = "分配的检验人员", example = "4880")
    private String assignmentId;

    @Schema(description = "分配的检验班组", example = "4880")
    private String assignmentTeamId;

    @Schema(description = "分配日期")
    @ExcelProperty("分配日期")
    private LocalDateTime assignmentDate;

    @Schema(description = "质检状态  0待派工1待检验2检验中3检验完成", example = "1")
    @ExcelProperty("质检状态  0待派工1待检验2检验中3检验完成")
    private Integer status;

    @Schema(description = "方案类型 来料检验  生产检验", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @ExcelProperty("方案类型 来料检验  生产检验")
    private Integer schemeType;

    @Schema(description = "检验类型1抽检2全检", example = "1")
    @ExcelProperty("检验类型1抽检2全检")
    private Integer inspectionSheetType;

    @Schema(description = "是否首检")
    @ExcelProperty("是否首检")
    private Integer needFirstInspection;

    @Schema(description = "物料类型ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "32568")
    @ExcelProperty("物料类型ID")
    private String materialConfigId;

    @Schema(description = "物料编号")
    private String materialNumber;

    @Schema(description = "物料类码")
    private String materialCode;

    @Schema(description = "物料名称")
    private String materialName;

    @Schema(description = "物料属性（成品、毛坯、辅助材料）")
    private Integer materialProperty;

    @Schema(description = "物料类型")
    private String materialType;

    @Schema(description = "物料类型（零件、托盘、工装、夹具、刀具）")
    private String materialTypeName;

    @Schema(description = "物料规格")
    private String materialSpecification;

    @Schema(description = "物料品牌")
    private String materialBrand;

    @Schema(description = "物料单位")
    private String materialUnit;

    @Schema(description = "工艺ID", example = "8843")
    private String technologyId;

    @Schema(description = "工序ID", example = "25350")
    private String processId;

    @Schema(description = "检验方案ID", example = "31934")
    private String inspectionSchemeId;

    @Schema(description = "通过准则")
    @ExcelProperty("通过准则")
    private String passRule;

    @Schema(description = "计划检验时间")
    @ExcelProperty("计划检验时间")
    private LocalDateTime planTime;

    @Schema(description = "实际开始时间")
    @ExcelProperty("实际开始时间")
    private LocalDateTime beginTime;

    @Schema(description = "实际结束时间")
    @ExcelProperty("实际结束时间")
    private LocalDateTime endTime;

    @Schema(description = "检测数量")
    @ExcelProperty("检测数量")
    private BigDecimal inspectionQuantity;

    @Schema(description = "合格数量")
    @ExcelProperty("合格数量")
    private Integer qualifiedQuantity;

    @Schema(description = "检测结果 1合格 2不合格")
    @ExcelProperty("检测结果 1合格 2不合格")
    private Integer inspectionResult;

    @Schema(description = "分配的检验班组", example = "4880")
    @ExcelProperty("检验班组")
    private String assignmentTeamName;

    @Schema(description = "分配的检验人员", example = "4880")
    @ExcelProperty("检验人员")
    private String assignmentName;

    @Schema(description = "总数量")
    private Integer quantity;

    @Schema(description = "检验水平类型")
    private Integer inspectionLevelType;

    @Schema(description = "类型 1正常检查2加严检查3放宽检查")
    private Integer samplingRuleType;

    @Schema(description = "物料批次号")
    private String batchNumber;

    @Schema(description = "是否自检")
    private Integer selfInspection;

    @Schema(description = "分配类型 1人员 2班组", example = "1")
    private Integer selfAssignmentType;

    @Schema(description = "分配的检验人员", example = "4880")
    private String selfAssignmentId;

    @Schema(description = "分配日期")
    private LocalDateTime selfAssignmentDate;

    @Schema(description = "工艺规程名称")
    private String processName;

    @Schema(description = "工序名称(加工路线)")
    private String procedureName;

    @Schema(description = "检验单名称")
    private String sheetName;

    @Schema(description = "检验单号")
    private String sheetNo;

    @Schema(description = "生产单号")
    private String recordNumber;

    @Schema(description = "不合格品审批状态")
    private String processStatus;

    @Schema(description = "检验单来源")
    private Integer sourceType;

}
