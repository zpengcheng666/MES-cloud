package com.miyu.module.pdm.controller.admin.processPlanDetail.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Set;

@Schema(description = "管理后台 - PDM 工步自定义属性 Request VO")
@Data
public class CustomizedAttributeValReqVO {

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
