package com.miyu.module.qms.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Schema(description = "RPC 服务 - 质量系统 检验方案 Resquest DTO")
@Data
public class InspectionSchemeReqDTO {

    @Schema(description = "物料类型", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @NotEmpty(message = "物料类型不能为空")
    private String materialConfigId;


    @Schema(description = "方案类型 1来料检验  2生产检验 3成品检验", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @NotNull(message = "方案类型不能为空")
    private Integer schemeType;


    @Schema(description = "工序ID")
    private String processId;

}