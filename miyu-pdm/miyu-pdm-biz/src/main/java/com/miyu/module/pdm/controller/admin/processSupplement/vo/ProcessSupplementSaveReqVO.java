package com.miyu.module.pdm.controller.admin.processSupplement.vo;

import com.mzt.logapi.starter.annotation.DiffLogField;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Schema(description = "pdm - 补加工工艺规程创建/更新 Request VO")
@Data
public class ProcessSupplementSaveReqVO {
    @Schema(description = "补加工工艺规程ID", example = "1")
    private String id;

    @Schema(description = "项目号")
    private String projectCode;

    @Schema(description = "零件图号")
    private String partNumber;

    @Schema(description = "工艺规程版本Id")
    private String processVersionId;

    @Schema(description = "当前工序id")
    private String procedureId;

    @Schema(description = "当前工序号")
    private String procedureNum;

    @Schema(description = "补加工后工序id")
    private String procedureIdNext;

    @Schema(description = "补加工后工序号")
    private String procedureNumNext;

    @Schema(description = "补加工工艺规程编号")
    private String processCodeSupplement;

    @Schema(description = "工作说明")
    private String description;

    @Schema(description = "状态")
    private String status;

}
