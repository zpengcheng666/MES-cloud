package com.miyu.module.mcc.controller.admin.materialtype.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import javax.validation.constraints.*;

@Schema(description = "管理后台 - 编码类别属性表(树形结构)新增/修改 Request VO")
@Data
public class MaterialTypeSaveReqVO {

    @Schema(description = "ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "32256")
    private String id;

    @Schema(description = "编码")
    private String code;

    @Schema(description = "名称", example = "王五")
    private String name;

    @Schema(description = "父类型id", requiredMode = Schema.RequiredMode.REQUIRED, example = "24179")
    @NotEmpty(message = "父类型id不能为空")
    private String parentId;

    @Schema(description = "位数", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer bitNumber;

    /***
     * 层级
     */
    @Schema(description = "层级", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer level;
    /**
     * 总层级
     */
    @Schema(description = "限制层级", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer levelLimit;

    @Schema(description = "分类", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "分类不能为空")
    private Integer encodingProperty;

}