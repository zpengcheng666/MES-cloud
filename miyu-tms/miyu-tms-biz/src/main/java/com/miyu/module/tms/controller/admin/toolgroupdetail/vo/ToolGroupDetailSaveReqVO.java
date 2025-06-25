package com.miyu.module.tms.controller.admin.toolgroupdetail.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Schema(description = "管理后台 - 刀具组装新增/修改 Request VO")
@Data
public class ToolGroupDetailSaveReqVO {

    @Schema(description = "ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "29203")
    private String id;

    @Schema(description = "成品刀具类型id", example = "25360")
    private String mainConfigId;

    @Schema(description = "刀位（非必填）")
    private Integer site;

    @Schema(description = "装配刀具类型id", example = "9521")
    private String accessoryConfigId;

    @Schema(description = "数量(仅限配件使用)", example = "15317")
    private Integer count;

}
