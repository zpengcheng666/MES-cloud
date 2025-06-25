package com.miyu.module.mcc.controller.admin.encodingrule.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Schema(description = "管理后台 - 编码记录新增/修改 Request VO")
@Data
public class CodeRecordStatusReqVO {


    @Schema(description = "编码", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotEmpty(message = "编码不能为空")
    private String code;



    @Schema(description = "状态  1 预生成 2 已使用  3释放", requiredMode = Schema.RequiredMode.REQUIRED, example = "2")
    @NotNull(message = "状态  1 预生成 2 已使用  3释放不能为空")
    private Integer status;

}