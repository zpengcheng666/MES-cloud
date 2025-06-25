package com.miyu.module.pdm.controller.admin.processPlanDetail.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Set;

@Schema(description = "管理后台 - PDM 工步自定义属性 Request VO")
@Data
public class CustomizedAttributeValRespVO {

    @Schema(description = "属性值id")
    private String id;

    @Schema(description = "关联对象id(如工步id)")
    private String objectId;

    @Schema(description = "属性id")
    private String attributeId;

    @Schema(description = "属性值json")
    private Set<String> attributeValue;

}
