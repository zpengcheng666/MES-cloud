package com.miyu.module.pdm.controller.admin.feasibilityDetail.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.ToString;

@Schema(description = "管理后台 - 单件定额管理 Request VO")
@Data
@ToString(callSuper = true)
public class QuotaPerPartReqVO {

    @Schema(description = "零部件版本id", example = "A220")
    private String partVersionId;

}
