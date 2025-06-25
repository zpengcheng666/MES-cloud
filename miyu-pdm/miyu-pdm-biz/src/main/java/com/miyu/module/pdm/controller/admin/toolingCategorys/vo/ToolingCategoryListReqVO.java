package com.miyu.module.pdm.controller.admin.toolingCategorys.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema(description = "管理后台 - 工装分类信息列表 Request VO")
@Data
public class ToolingCategoryListReqVO {
    @Schema(description = "分类名称", example = "芋艿")
    private String name;

    @Schema(description = "开启状态", example = "1")
    private Integer status;
}
