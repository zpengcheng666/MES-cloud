package com.miyu.module.qms.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema(description = "RPC 服务 - 质量系统 不合格品 Response DTO")
@Data
public class UnqualifiedMaterialRespDTO {

    @Schema(description = "不合格品主键", example = "1")
    private String id;

    @Schema(description = "严重等级")
    private Integer defectiveLevel;

    @Schema(description = "处理方式")
    private Integer handleMethod;

    @Schema(description = "缺陷描述")
    private String content;

    @Schema(description = "库存主键")
    private String materialId;

    @Schema(description = "物料条码")
    private String barCode;

}
