package com.miyu.module.pdm.controller.admin.structureDefinition.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema(description = "PDM - 数据包结构列表 Request VO")
@Data
public class StructureListReqVO {

    @Schema(description = "结构名称，模糊匹配", example = "芋道")
    private String name;

    @Schema(description = "展示状态，参见 CommonStatusEnum 枚举类", example = "1")
    private Integer status;

    @Schema(description = "节点类型", example = "1")
    private Integer type;

    @Schema(description = "结构编号", example = "1")
    private Long id;
}
