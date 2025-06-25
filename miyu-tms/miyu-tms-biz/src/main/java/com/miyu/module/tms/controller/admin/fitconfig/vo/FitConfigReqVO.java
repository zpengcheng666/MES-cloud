package com.miyu.module.tms.controller.admin.fitconfig.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.ToString;

@Schema(description = "管理后台 - 刀具适配分页 Request VO")
@Data
@ToString(callSuper = true)
public class FitConfigReqVO {

    @Schema(description = "刀具类型id", example = "22693")
    private String toolConfigId;

    @Schema(description = "适配类型id", example = "21882")
    private String fitToolConfigId;

    @Schema(description = "参数模板id", example = "21882")
    private String templateId;

}
