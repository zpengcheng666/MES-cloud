package com.miyu.module.mcc.api.materialtype.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
@Schema(description = "RPC 服务 - 编码类别属性")
@Data
public class MaterialTypeRespDTO{

    @Schema(description = "ID")
    private String id;

    @Schema(description = "编码")
    private String code;

    @Schema(description = "名称")
    private String name;

    @Schema(description = "父类型id")
    private String parentId;

    @Schema(description = "位数")
    private Integer bitNumber;

    @Schema(description = "层级")
    private Integer level;

    @Schema(description = "分类")
    private Integer encodingProperty;




}
