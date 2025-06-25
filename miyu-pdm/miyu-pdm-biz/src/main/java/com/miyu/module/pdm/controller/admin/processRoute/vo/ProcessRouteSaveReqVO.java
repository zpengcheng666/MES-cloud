package com.miyu.module.pdm.controller.admin.processRoute.vo;

import com.mzt.logapi.starter.annotation.DiffLogField;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Schema(description = "pdm - 加工路线创建/更新 Request VO")
@Data
public class ProcessRouteSaveReqVO {
    @Schema(description = "加工路线ID", example = "1")
    private String id;

    @Schema(description = "加工路线名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "路线A")
    @NotBlank(message = "加工路线名称不能为空")
    @Size(max = 30, message = "加工路线名称长度不能超过 30 个字符")
    @DiffLogField(name = "加工路线名称")
    private String name;

    @Schema(description = "加工路线描述", requiredMode = Schema.RequiredMode.REQUIRED, example = "描述。。。。")
    @NotBlank(message = "加工路线名称不能为空")
    @Size(max = 100, message = "加工路线名称长度不能超过 100 个字符")
    @DiffLogField(name = "加工路线名称")
    private String description;

}
