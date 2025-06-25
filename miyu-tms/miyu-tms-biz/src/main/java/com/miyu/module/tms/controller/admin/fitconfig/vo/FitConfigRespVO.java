package com.miyu.module.tms.controller.admin.fitconfig.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import com.alibaba.excel.annotation.*;

@Schema(description = "管理后台 - 刀具适配 Response VO")
@Data
@ExcelIgnoreUnannotated
public class FitConfigRespVO {

    @Schema(description = "ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "14740")
    private String id;

    @Schema(description = "刀具类型id", example = "22693")
    private String toolConfigId;

    @Schema(description = "适配类型id")
    private String fitToolConfigId;

    @Schema(description = "参数模板id")
    private String templateId;

    @Schema(description = "名称")
    private String toolName;

    @Schema(description = "型号")
    private String toolModel;

    @Schema(description = "刀具类码")
    private String materialTypeCode;

}
