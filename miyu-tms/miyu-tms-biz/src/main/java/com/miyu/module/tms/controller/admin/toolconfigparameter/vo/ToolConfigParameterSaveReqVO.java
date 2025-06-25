package com.miyu.module.tms.controller.admin.toolconfigparameter.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import javax.validation.constraints.*;

@Schema(description = "管理后台 - 刀具参数信息新增/修改 Request VO")
@Data
public class ToolConfigParameterSaveReqVO {

    @Schema(description = "主键", requiredMode = Schema.RequiredMode.REQUIRED, example = "25441")
    private Long id;

    @Schema(description = "刀具ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "7161")
    @NotNull(message = "刀具ID不能为空")
    private Long toolInformationId;

    @Schema(description = "参数名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "赵六")
    @NotEmpty(message = "参数名称不能为空")
    private String name;

    @Schema(description = "参数值", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotEmpty(message = "参数值不能为空")
    private String value;

    @Schema(description = "参数缩写")
    private String abbr;

    @Schema(description = "计量单位")
    private String unit;

    @Schema(description = "排序", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "排序不能为空")
    private Integer sort;

    @Schema(description = "分类 1 几何参数  2 切削参数", example = "1")
    private Boolean type;

}
