package com.miyu.module.pdm.controller.admin.part.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.ToString;

@Schema(description = "管理后台 - 零件设计属性 Request VO")
@Data
@ToString(callSuper = true)
public class PartAttrReqVO {

    @Schema(description = "产品id", example = "20041")
    private String rootProductId;

    @Schema(description = "客户化标识", example = "std")
    private String customizedIndex;

    @Schema(description = "零部件版本id", example = "20041")
    private String partVersionId;

}
