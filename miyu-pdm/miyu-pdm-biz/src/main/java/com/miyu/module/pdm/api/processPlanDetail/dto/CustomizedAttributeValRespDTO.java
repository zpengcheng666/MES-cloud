package com.miyu.module.pdm.api.processPlanDetail.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Set;

@Schema(description = "RPC 服务 - 工步自定义属性值")
@Data
public class CustomizedAttributeValRespDTO {

    @Schema(description = "属性id")
    private String id;

    @Schema(description = "属性中文名")
    private String attrNameCn;

    @Schema(description = "属性值")
    private Set<String> attrDefaultValue;

    @Schema(description = "是否多值属性 1-是 0-否 ")
    private Integer isMultvalues;

    @Schema(description = "有效值数组")
    private String attrEffectiveValue;
}
