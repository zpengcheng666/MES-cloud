package com.miyu.module.mcc.controller.admin.encodingclassification.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import javax.validation.constraints.*;

@Schema(description = "管理后台 - 编码分类新增/修改 Request VO")
@Data
public class EncodingClassificationSaveReqVO {

    @Schema(description = "ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "7743")
    private String id;

    @Schema(description = "编码分类CODE", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotEmpty(message = "编码分类CODE不能为空")
    private String code;

    @Schema(description = "编码分类名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "李四")
    @NotEmpty(message = "编码分类名称不能为空")
    private String name;

    @Schema(description = "分类所属服务", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotEmpty(message = "分类所属服务不能为空")
    private String service;

    @Schema(description = "分类查看编码使用地址", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotEmpty(message = "分类查看编码使用地址不能为空")
    private String path;

}