package com.miyu.module.mcc.controller.admin.encodingattribute.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import javax.validation.constraints.*;

@Schema(description = "管理后台 - 编码自定义属性新增/修改 Request VO")
@Data
public class EncodingAttributeSaveReqVO {

    @Schema(description = "ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "32519")
    private String id;

    @Schema(description = "属性名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "芋艿")
    @NotEmpty(message = "属性名称不能为空")
    private String name;

    @Schema(description = "属性值", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotEmpty(message = "属性值不能为空")
    private String code;

}