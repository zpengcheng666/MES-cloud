package com.miyu.module.mcc.controller.admin.coderecord.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import javax.validation.constraints.*;

@Schema(description = "管理后台 - 编码记录新增/修改 Request VO")
@Data
public class CodeRecordSaveReqVO {

    @Schema(description = "ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "3984")
    private String id;

    @Schema(description = "编码", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotEmpty(message = "编码不能为空")
    private String code;

    @Schema(description = "名称", example = "赵六")
    private String name;

    @Schema(description = "父类型id", requiredMode = Schema.RequiredMode.REQUIRED, example = "16564")
    @NotEmpty(message = "父类型id不能为空")
    private String parentId;

    @Schema(description = "状态  1 预生成 2 已使用  3释放", requiredMode = Schema.RequiredMode.REQUIRED, example = "2")
    @NotNull(message = "状态  1 预生成 2 已使用  3释放不能为空")
    private Integer status;

    @Schema(description = "编码规则ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "15408")
    @NotEmpty(message = "编码规则ID不能为空")
    private String encodingRuleId;

    @Schema(description = "编码分类ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "3088")
    @NotEmpty(message = "编码分类ID不能为空")
    private String classificationId;


    private String params;

}