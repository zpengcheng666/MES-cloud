package com.miyu.module.qms.controller.admin.inspectionitem.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.time.LocalDateTime;
import com.alibaba.excel.annotation.*;

@Schema(description = "管理后台 - 检测项目 Response VO")
@Data
@ExcelIgnoreUnannotated
public class InspectionItemRespVO {

    @Schema(description = "主键", requiredMode = Schema.RequiredMode.REQUIRED, example = "17602")
    @ExcelProperty("主键")
    private String id;

    @Schema(description = "创建时间", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("创建时间")
    private LocalDateTime createTime;

    @Schema(description = "检测项目名称", example = "赵六")
    @ExcelProperty("检测项目名称")
    private String itemName;

    @Schema(description = "检测项目编号")
    @ExcelProperty("检测项目编号")
    private String itemNo;

    @Schema(description = "检测项目描述")
    @ExcelProperty("检测项目描述")
    private String content;

    @Schema(description = "检测方式  1定性 2定量", example = "1")
    @ExcelProperty("检测方式  1定性 2定量")
    private Integer inspectionType;

    @Schema(description = "检测工具  目测 皮尺测量 ")
    private String inspectionToolId;

    @Schema(description = "检测项目分类ID", example = "30665")
    @ExcelProperty("检测项目分类ID")
    private String itemTypeId;


    @Schema(description = "检测项目分类名称", example = "30665")
    @ExcelProperty("检测项目分类名称")
    private String itemTypeName;

    @Schema(description = "检测工具  目测 皮尺测量 ")
    @ExcelProperty("检测工具")
    private String inspectionToolName;

    @Schema(description = "物料类型主键 ")
    private String materialConfigId;

}