package com.miyu.module.qms.controller.admin.defectivecode.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Schema(description = "管理后台 - 缺陷代码新增/修改 Request VO")
@Data
public class DefectiveCodeSaveReqVO {

    @Schema(description = "主键", requiredMode = Schema.RequiredMode.REQUIRED, example = "14495")
    private String id;

    @Schema(description = "缺陷代码名称", example = "王五")
    private String name;

    @Schema(description = "缺陷代码")
    private String code;

}