package com.miyu.module.qms.controller.admin.inspectionsheetschemematerial.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.time.LocalDateTime;
import com.alibaba.excel.annotation.*;

@Schema(description = "管理后台 - 检验单产品 Response VO")
@Data
@ExcelIgnoreUnannotated
public class InspectionSheetSchemeMaterialRespVO {

    @Schema(description = "主键", requiredMode = Schema.RequiredMode.REQUIRED, example = "28969")
    @ExcelProperty("主键")
    private String id;

    @Schema(description = "创建时间", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("创建时间")
    private LocalDateTime createTime;

    @Schema(description = "测量结果")
    @ExcelProperty("测量结果")
    private String content;

    @Schema(description = "是否合格")
    @ExcelProperty("是否合格")
    private Integer inspectionResult;

    @Schema(description = "物料ID", example = "3274")
    @ExcelProperty("物料ID")
    private String materialId;

    @Schema(description = "物料类型ID", example = "12392")
    @ExcelProperty("物料类型ID")
    private String materialConfigId;

    @Schema(description = "物料条码")
    @ExcelProperty("物料条码")
    private String barCode;

    @Schema(description = "物料批次号")
    @ExcelProperty("物料批次号")
    private String batchNumber;

    @Schema(description = "物料条码")
    private String barCodeCheck;

    @Schema(description = "检验单任务表ID")
    private String inspectionSheetSchemeId;

    @Schema(description = "物料编号")
    private String materialNumber;

    @Schema(description = "物料名称")
    private String materialName;

    @Schema(description = "物料类型（零件、托盘、工装、夹具、刀具）")
    private Integer materialType;

    @Schema(description = "物料类型")
    private String materialTypeName;

    @Schema(description = "物料规格")
    private String materialSpecification;

    @Schema(description = "物料单位")
    private String materialUnit;

    @Schema(description = "质检状态")
    private Integer status;

    @Schema(description = "分配的检验人员")
    private String assignmentId;

    @Schema(description = "分配的检验人员姓名")
    private String assignmentName;

    @Schema(description = "分配日期")
    private LocalDateTime assignmentDate;

    @Schema(description = "工艺ID")
    private String technologyId;

    @Schema(description = "工序ID")
    private String processId;

    @Schema(description = "检验单名称")
    private String sheetName;

    @Schema(description = "检验单号")
    private String sheetNo;

    @Schema(description = "不合格品主键")
    private String unqualifiedMaterialId;

    @Schema(description = "缺陷代码")
    private String defectiveCode;

    @Schema(description = "缺陷等级")
    private Integer defectiveLevel;

    @Schema(description = "不合格品处理方式")
    private Integer handleMethod;
}
