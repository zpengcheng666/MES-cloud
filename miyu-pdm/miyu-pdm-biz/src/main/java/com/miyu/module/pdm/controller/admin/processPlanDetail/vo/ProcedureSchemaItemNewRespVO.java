package com.miyu.module.pdm.controller.admin.processPlanDetail.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Schema(description = "PDM - 工序检测项目详情 Response VO")
@Data
public class ProcedureSchemaItemNewRespVO {

    @Schema(description = "检验方案id")
    private String inspectionSchemeId;

    @Schema(description = "检验项列表", example = "1")
    private List<ProcedureSchemaItemRespVO> itemList;

}
