package com.miyu.module.qms.controller.admin.unqualifiedmaterial.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Schema(description = "管理后台 - 不合格品产品新增/修改 Request VO")
@Data
public class UnqualifiedMaterialSaveReqVO {

    @Schema(description = "主键", requiredMode = Schema.RequiredMode.REQUIRED, example = "4912")
    private String id;

    @Schema(description = "检验单方案任务ID", example = "4041")
    private String inspectionSheetSchemeId;

    @Schema(description = "检验单产品ID", example = "26336")
    private String schemeMaterialId;

    @Schema(description = "不合格品登记ID", example = "13208")
    private String unqualifiedRegistrationId;

}