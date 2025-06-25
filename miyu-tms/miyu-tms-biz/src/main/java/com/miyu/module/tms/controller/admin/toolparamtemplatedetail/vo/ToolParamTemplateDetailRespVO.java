package com.miyu.module.tms.controller.admin.toolparamtemplatedetail.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.time.LocalDateTime;
import com.alibaba.excel.annotation.*;

@Schema(description = "管理后台 - 参数模版详情 Response VO")
@Data
@ExcelIgnoreUnannotated
public class ToolParamTemplateDetailRespVO {

    @Schema(description = "主键", requiredMode = Schema.RequiredMode.REQUIRED, example = "15238")
    @ExcelProperty("主键")
    private String id;

    @Schema(description = "创建时间", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("创建时间")
    private LocalDateTime createTime;

    @Schema(description = "参数模板主表ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "23730")
    @ExcelProperty("参数模板主表ID")
    private String paramTemplateId;

    @Schema(description = "参数名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "李四")
    @ExcelProperty("参数名称")
    private String name;

    @Schema(description = "参数缩写")
    @ExcelProperty("参数缩写")
    private String abbr;

    @Schema(description = "计量单位")
    @ExcelProperty("计量单位")
    private String unit;

    @Schema(description = "排序")
    @ExcelProperty("排序")
    private Integer sort;

    @Schema(description = "分类 1 几何参数  2 切削参数", requiredMode = Schema.RequiredMode.REQUIRED, example = "2")
    @ExcelProperty("分类 1 几何参数  2 切削参数")
    private Integer type;

    @Schema(description = "是否必填  1 否 2 是", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("是否必填  1 否 2 是")
    private Boolean required;

    @Schema(description = "默认参数值（下拉框时）")
    @ExcelProperty("默认参数值（下拉框时）")
    private String defaultValue;

}
