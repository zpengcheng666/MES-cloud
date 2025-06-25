package com.miyu.module.tms.controller.admin.fitconfig.vo;

import lombok.*;
import io.swagger.v3.oas.annotations.media.Schema;
import cn.iocoder.yudao.framework.common.pojo.PageParam;

@Schema(description = "管理后台 - 刀具适配分页 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class FitConfigPageReqVO extends PageParam {

    @Schema(description = "刀具类型id", example = "22693")
    private String toolConfigId;

    @Schema(description = "适配类型id", example = "21882")
    private String fitToolConfigId;

    @Schema(description = "参数模板id", example = "21882")
    private String templateId;

    @Schema(description = "刀具类别ID", example = "21882")
    private String materialTypeId;

    @Schema(description = "刀具编号", example = "21882")
    private String toolNumber;

    @Schema(description = "刀具类码", example = "21882")
    private String toolTypeCode;

}
