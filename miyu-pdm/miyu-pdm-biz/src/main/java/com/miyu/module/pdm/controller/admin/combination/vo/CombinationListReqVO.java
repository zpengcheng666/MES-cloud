package com.miyu.module.pdm.controller.admin.combination.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Schema(description = "管理后台 - PDM 刀组列表-临时 Request VO")
@Data
public class CombinationListReqVO {

    @Schema(description = "刀组编码", example = "芋艿")
    private String cutterGroupCode;

    @Schema(description = "刀柄类型", example = "芋艿")
    private String taperTypeName;

    @Schema(description = "刀柄通用规格", example = "芋艿")
    private String hiltMark;

}
