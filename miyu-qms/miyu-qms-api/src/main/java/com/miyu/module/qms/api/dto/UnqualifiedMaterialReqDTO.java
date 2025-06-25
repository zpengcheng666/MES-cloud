package com.miyu.module.qms.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema(description = "RPC 服务 - 质量系统 不合格品 Resquest DTO")
@Data
public class UnqualifiedMaterialReqDTO {

    @Schema(description = "不合格品主键", example = "1")
    private String id;

    @Schema(description = "严重等级")
    private Integer defectiveLevel;

    @Schema(description = "处理方式")
    private Integer handleMethod;

}