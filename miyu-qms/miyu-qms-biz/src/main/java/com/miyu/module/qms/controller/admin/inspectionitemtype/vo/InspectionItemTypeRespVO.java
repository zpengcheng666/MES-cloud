package com.miyu.module.qms.controller.admin.inspectionitemtype.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import java.util.*;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;
import com.alibaba.excel.annotation.*;

@Schema(description = "管理后台 - 检测项目分类 Response VO")
@Data
@ExcelIgnoreUnannotated
public class InspectionItemTypeRespVO {

    @Schema(description = "主键", requiredMode = Schema.RequiredMode.REQUIRED, example = "12930")
    @ExcelProperty("主键")
    private String id;

    @Schema(description = "创建时间", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("创建时间")
    private LocalDateTime createTime;

    @Schema(description = "父项目分类ID", example = "6128")
    @ExcelProperty("父项目分类ID")
    private String parentId;

    @Schema(description = "检测项目分类名称", example = "李四")
    @ExcelProperty("检测项目分类名称")
    private String name;

}