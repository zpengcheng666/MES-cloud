package com.miyu.module.pdm.controller.admin.feasibilityDetail.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema(description = "PDM - 技术评估结果 Request VO")
@Data
public class FeasibilityResultSaveReqVO {

    @Schema(description = "id", example = "1")
    private String id;

    @Schema(description = "项目编号")
    private String projectCode;

    @Schema(description = "零部件版本ID")
    private String partVersionId;

    @Schema(description = "评估结果 0:通过 1:不通过")
    private Integer isPass;

    @Schema(description = "不通过原因")
    private String reason;

}
