package com.miyu.module.pdm.controller.admin.product.vo;

import com.mzt.logapi.starter.annotation.DiffLogField;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Schema(description = "PDM - 产品创建/更新 Request VO")
@Data
public class ProductSaveReqVO {

    @Schema(description = "产品ID", example = "1")
    private String id;

    @Schema(description = "产品名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "C系列")
    @NotBlank(message = "产品名称不能为空")
    @Size(max = 30, message = "产品名称长度不能超过 30 个字符")
    @DiffLogField(name = "产品名称")
    private String productName;

    @NotBlank(message = "产品图号不能为空")
    @Size(max = 30, message = "产品图号长度不能超过 30 个字符")
    @Schema(description = "产品图号", requiredMode = Schema.RequiredMode.REQUIRED, example = "A220")
    @DiffLogField(name = "产品图号")
    private String productNumber;

    @Schema(description = "设计单位", example = "庞巴迪")
    @DiffLogField(name = "设计单位")
    private String designUnit;

    @Schema(description = "产品状态", example = "1")
    @DiffLogField(name = "产品状态")
    private Integer status;

    @Schema(description = "产品描述", example = "我是一个产品")
    @DiffLogField(name = "产品描述")
    private String description;

    @Schema(description = "产品分类编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "11161")
    @NotNull(message = "产品分类编号不能为空")
    private Long categoryId;

}
