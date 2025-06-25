package com.miyu.module.qms.controller.admin.retraceconfig.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import javax.validation.constraints.*;

@Schema(description = "管理后台 - 追溯字段配置新增/修改 Request VO")
@Data
public class RetraceConfigSaveReqVO {

    @Schema(description = "主键", requiredMode = Schema.RequiredMode.REQUIRED, example = "27061")
    private String id;

    @Schema(description = "字段名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "张三")
    @NotEmpty(message = "字段名称不能为空")
    private String name;

    @Schema(description = "字段属性")
    private String no;

}