package com.miyu.module.pdm.controller.admin.processRouteTypical.vo;

import com.mzt.logapi.starter.annotation.DiffLogField;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Schema(description = "pdm - 典型工艺路线创建/更新 Request VO")
@Data
public class ProcessRouteTypicalSaveReqVO {
    @Schema(description = "典型工艺路线ID", example = "1")
    private String id;

    @Schema(description = "典型工艺路线名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "路线A")
    @NotBlank(message = "典型工艺路线名称不能为空")
    @Size(max = 30, message = "典型工艺路线名称长度不能超过 30 个字符")
    @DiffLogField(name = "典型工艺路线名称")
    private String name;

    @Schema(description = "工序名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "路线A")
    @NotBlank(message = "工序名称不能为空")
    @Size(max = 30, message = "工序名称长度不能超过 30 个字符")
    @DiffLogField(name = "工序名称")
    private String procedureName;

    @Schema(description = "工艺路线", requiredMode = Schema.RequiredMode.REQUIRED, example = "路线A")
    @NotBlank(message = "工艺路线不能为空")
    @DiffLogField(name = "工艺路线")
    private String processRouteName;

    @Schema(description = "典型工艺路线描述", requiredMode = Schema.RequiredMode.REQUIRED, example = "描述。。。。")
    @NotBlank(message = "典型工艺路线描述不能为空")
    @Size(max = 100, message = "典型工艺路线描述长度不能超过 100 个字符")
    @DiffLogField(name = "典型工艺路线描述")
    private String description;

}
