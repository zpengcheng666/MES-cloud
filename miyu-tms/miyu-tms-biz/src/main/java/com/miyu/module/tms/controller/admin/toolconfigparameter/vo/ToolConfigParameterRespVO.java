package com.miyu.module.tms.controller.admin.toolconfigparameter.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import java.util.*;
import com.alibaba.excel.annotation.*;

@Schema(description = "管理后台 - 刀具参数信息 Response VO")
@Data
@ExcelIgnoreUnannotated
public class ToolConfigParameterRespVO {

    @Schema(description = "主键", requiredMode = Schema.RequiredMode.REQUIRED, example = "25441")
    @ExcelProperty("主键")
    private Long id;

    @Schema(description = "刀具ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "7161")
    @ExcelProperty("刀具ID")
    private Long toolInformationId;

    @Schema(description = "参数名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "赵六")
    @ExcelProperty("参数名称")
    private String name;

    @Schema(description = "参数值", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("参数值")
    private String value;

    @Schema(description = "参数缩写")
    @ExcelProperty("参数缩写")
    private String abbr;

    @Schema(description = "计量单位")
    @ExcelProperty("计量单位")
    private String unit;

    @Schema(description = "排序", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("排序")
    private Integer sort;

    @Schema(description = "分类 1 几何参数  2 切削参数", example = "1")
    @ExcelProperty("分类 1 几何参数  2 切削参数")
    private Boolean type;

}
