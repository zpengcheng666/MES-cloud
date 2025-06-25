package com.miyu.module.pdm.controller.admin.feasibilityDetail.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.ToString;

@Schema(description = "管理后台 - 技术评估结果 Request VO")
@Data
@ToString(callSuper = true)
public class FeasibilityResultReqVO {

    @Schema(description = "项目编号", example = "20041")
    private String projectCode;

    @Schema(description = "零部件版本id", example = "A220")
    private String partVersionId;

}
