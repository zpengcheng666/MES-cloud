package com.miyu.module.qms.controller.admin.inspectionsheetrecord.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import com.alibaba.excel.annotation.*;

@Schema(description = "管理后台 - 检验记录 Response VO")
@Data
@ExcelIgnoreUnannotated
public class InspectionSheetRecordRespVO {

    @Schema(description = "主键", requiredMode = Schema.RequiredMode.REQUIRED, example = "30944")
    @ExcelProperty("主键")
    private String id;

    @Schema(description = "创建时间", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("创建时间")
    private LocalDateTime createTime;

    @Schema(description = "检测项目ID", example = "21030")
    @ExcelProperty("检测项目ID")
    private String inspectionItemId;

    @Schema(description = "检测项ID", example = "17919")
    @ExcelProperty("检测项ID")
    private String inspectionItemDetailId;

    @Schema(description = "检验单ID", example = "4647")
    @ExcelProperty("检验单ID")
    private String inspectionSheetId;

    @Schema(description = "测量结果")
    @ExcelProperty("测量结果")
    private String content;

    @Schema(description = "是否合格")
    @ExcelProperty("是否合格")
    private Integer inspectionResult;

    @Schema(description = "互检测量结果")
    @ExcelProperty("互检测量结果")
    private String mutualContent;

    @Schema(description = "互检是否合格")
    @ExcelProperty("互检是否合格")
    private Integer mutualInspectionResult;

    @Schema(description = "专检测量结果")
    @ExcelProperty("专检测量结果")
    private String specContent;

    @Schema(description = "专检是否合格")
    @ExcelProperty("专检是否合格")
    private Integer specInspectionResult;

    @Schema(description = "检验单物料表ID", example = "18307")
    @ExcelProperty("检验单物料表ID")
    private String schemeMaterialId;

    @Schema(description = "检测项名称")
    private String inspectionSchemeItemName;

    @Schema(description = "检测项配置名称")
    private String inspectionItemTypeName;

    @Schema(description = "判断方式  文本   是否  数字  条件判断")
    private Integer referenceType;

    @Schema(description = "上限值")
    private BigDecimal schemeMaxValue;

    @Schema(description = "下限值")
    private BigDecimal schemeMinValue;

    @Schema(description = "技术要求")
    private String schemeContent;

    @Schema(description = "判断")
    private Integer judgement;

}