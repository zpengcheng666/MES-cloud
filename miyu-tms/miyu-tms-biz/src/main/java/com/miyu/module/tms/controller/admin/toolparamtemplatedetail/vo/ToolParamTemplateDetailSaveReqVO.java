package com.miyu.module.tms.controller.admin.toolparamtemplatedetail.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import javax.validation.constraints.*;

@Schema(description = "管理后台 - 参数模版详情新增/修改 Request VO")
@Data
public class ToolParamTemplateDetailSaveReqVO {

    @Schema(description = "主键", requiredMode = Schema.RequiredMode.REQUIRED, example = "15238")
    private String id;

    @Schema(description = "参数模板主表ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "23730")
    @NotEmpty(message = "参数模板主表ID不能为空")
    private String paramTemplateId;

    @Schema(description = "参数名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "李四")
    @NotEmpty(message = "参数名称不能为空")
    private String name;

    @Schema(description = "参数缩写")
    private String abbr;

    @Schema(description = "计量单位")
    private String unit;

    @Schema(description = "排序")
    private Integer sort;

    @Schema(description = "分类 1 几何参数  2 切削参数", requiredMode = Schema.RequiredMode.REQUIRED, example = "2")
    @NotNull(message = "分类 1 几何参数  2 切削参数不能为空")
    private Boolean type;

    @Schema(description = "是否必填  1 否 2 是", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "是否必填  1 否 2 是不能为空")
    private Boolean required;

    @Schema(description = "默认参数值（下拉框时）")
    private String defaultValue;

}