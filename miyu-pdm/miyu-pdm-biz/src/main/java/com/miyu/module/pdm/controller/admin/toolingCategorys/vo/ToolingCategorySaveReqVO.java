package com.miyu.module.pdm.controller.admin.toolingCategorys.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Schema(description = "管理后台 - 产品分类信息新增/修改 Request VO")
@Data
public class ToolingCategorySaveReqVO {

    @Schema(description = "分类编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "738")
    private Long id;

    @Schema(description = "父分类编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "17072")
    @NotNull(message = "父分类编号不能为空")
    private Long parentId;

    @Schema(description = "分类名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "王五")
    @NotEmpty(message = "分类名称不能为空")
    private String name;

    @Schema(description = "分类编码", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotEmpty(message = "分类编码不能为空")
    private String code;

    @Schema(description = "分类排序", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "分类排序不能为空")
    private Integer sort;

    @Schema(description = "开启状态（0正常 1停用）", requiredMode = Schema.RequiredMode.REQUIRED, example = "2")
    @NotNull(message = "开启状态（0正常 1停用）不能为空")
    private Integer status;

}