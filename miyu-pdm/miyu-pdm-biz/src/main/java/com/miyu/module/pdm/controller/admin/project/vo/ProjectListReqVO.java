package com.miyu.module.pdm.controller.admin.project.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema(description = "管理后台 - PDM 项目列表 Request VO")
@Data
public class ProjectListReqVO {

    @Schema(description = "项目名称", example = "芋艿")
    private String name;

    @Schema(description = "项目编号", example = "芋艿")
    private String code;
}
