package com.miyu.module.qms.controller.admin.inspectionsheet.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.time.LocalDateTime;
import com.alibaba.excel.annotation.*;

@Schema(description = "管理后台 - 检验单 Response VO")
@Data
@ExcelIgnoreUnannotated
public class InspectionSheetRespVO {

    @Schema(description = "主键", requiredMode = Schema.RequiredMode.REQUIRED, example = "14202")
    @ExcelProperty("主键")
    private String id;

    @Schema(description = "创建时间", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("创建时间")
    private LocalDateTime createTime;

    @Schema(description = "创建人")
    private String creator;
    private String creatorName;

    @Schema(description = "检验单名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "李四")
    @ExcelProperty("检验单名称")
    private String sheetName;

    @Schema(description = "检验单号")
    @ExcelProperty("检验单号")
    private String sheetNo;

    @Schema(description = "生产单号")
    @ExcelProperty("生产单号")
    private String recordNumber;

    @Schema(description = "检验单来源", example = "1")
    @ExcelProperty("检验单来源")
    private Integer sourceType;

    @Schema(description = "质检状态  0待派工1待检验2检验中3检验完成", example = "1")
    @ExcelProperty("质检状态  0待派工1待检验2检验中3检验完成")
    private Integer status;

    @Schema(description = "负责人")
    @ExcelProperty("负责人")
    private String header;

    @Schema(description = "开始时间")
    @ExcelProperty("开始时间")
    private LocalDateTime beginTime;

    @Schema(description = "结束时间")
    @ExcelProperty("结束时间")
    private LocalDateTime endTime;

    /**
     * 查询总检验单新增显示字段
     */
    @Schema(description = "物料类型ID")
    private String materialConfigId;

    @Schema(description = "物料编号")
    private String materialNumber;

    @Schema(description = "物料类码")
    private String materialCode;

    @Schema(description = "物料名称")
    private String materialName;

    @Schema(description = "物料属性（成品、毛坯、辅助材料）")
    private Integer materialProperty;

    @Schema(description = "物料类型（零件、托盘、工装、夹具、刀具）")
    private Integer materialType;

    @Schema(description = "物料类型（零件、托盘、工装、夹具、刀具）")
    private String materialTypeName;

    @Schema(description = "物料规格")
    private String materialSpecification;

    @Schema(description = "物料品牌")
    private String materialBrand;

    @Schema(description = "物料单位")
    private String materialUnit;

    @Schema(description = "方案类型 来料检验  生产检验")
    private Integer schemeType;

    @Schema(description = "检验类型1抽检2全检")
    private Integer inspectionSheetType;

    @Schema(description = "分配类型 1人员 2班组")
    private Integer assignmentType;

    @Schema(description = "分配的检验人员")
    private String assignmentId;
    private String assignmentName;

    @Schema(description = "分配日期")
    private LocalDateTime assignmentDate;

    @Schema(description = "产品ID")
    private String materialId;

    @Schema(description = "物料条码")
    private String barCode;

    @Schema(description = "检测数量")
    private Integer inspectionQuantity;

    @Schema(description = "实际检测数量")
    private Integer quantity;

    @Schema(description = "合格数量")
    private Integer qualifiedQuantity;

    @Schema(description = "检测结果")
    private Integer inspectionResult;

    @Schema(description = "通过准则")
    private String passRule;

    @Schema(description = "测量结果")
    private String content;

    @Schema(description = "检验任务质检状态")
    private Integer schemeStatus;

    @Schema(description = "是否自检")
    private Integer selfInspection;

    @Schema(description = "是否自检")
    private Integer selfAssignmentStatus;

    @Schema(description = "工艺ID", example = "8843")
    private String technologyId;

    @Schema(description = "工序ID", example = "25350")
    private String processId;

    @Schema(description = "工艺规程名称")
    private String processName;

    @Schema(description = "工序名称(加工路线)")
    private String technologyName;

}
