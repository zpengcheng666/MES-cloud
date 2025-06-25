package com.miyu.module.pdm.api.projectPlan.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema(description = "rpc - 工艺规程编制 Response DTO")
@Data
public class ProcedureDetailRespDTO {
    @Schema(description = "id", requiredMode = Schema.RequiredMode.REQUIRED, example = "6902")
    private String id;

    @Schema(description = "零件工艺规程版本id")
    private String processVersionId;

    @Schema(description = "工序Id")
    private String procedureId;

    @Schema(description = "制造资源类型：1设备 2刀具 3工装")
    private Integer resourcesType;

    @Schema(description = "制造资源类型ID")
    private String resourcesTypeId;

    @Schema(description = "数量")
    private String quantity;
}
