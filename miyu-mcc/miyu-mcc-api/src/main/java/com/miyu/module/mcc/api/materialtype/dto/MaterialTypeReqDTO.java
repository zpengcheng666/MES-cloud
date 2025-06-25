package com.miyu.module.mcc.api.materialtype.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NonNull;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Schema(description = "RPC 服务 - 编码类别属性")
@Data
public class MaterialTypeReqDTO {


    @Schema(description = "编码")
    @NotEmpty(message = "编码不能为空")
    private String code;

    @Schema(description = "名称")
    @NotEmpty(message = "名称不能为空")
    private String name;

    @Schema(description = "父类型id")
    @NotEmpty(message = "父类型id不能为空")
    private String parentId;

    @Schema(description = "分类属性")//参考 MaterialTypePropertyEnum  枚举
    @NotEmpty(message = "分类属性不能为空")
    private Integer encodingProperty;




}
