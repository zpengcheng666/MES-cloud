package com.miyu.module.ppm.controller.admin.material.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Schema(description = "管理后台 - 物料基本信息新增/修改 Request VO")
@Data
public class MaterialSaveReqVO {

    @Schema(description = "名臣", requiredMode = Schema.RequiredMode.REQUIRED, example = "7279")
    private String id;

    @Schema(description = "类型", example = "2")
    private Integer type;

    @Schema(description = "名称", example = "赵六")
    private String name;

    @Schema(description = "创建IP")
    private String creationIp;

    @Schema(description = "更新ip")
    private String updatedIp;

}