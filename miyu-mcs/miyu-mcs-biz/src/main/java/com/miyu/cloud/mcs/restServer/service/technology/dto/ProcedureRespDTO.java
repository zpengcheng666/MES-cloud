package com.miyu.cloud.mcs.restServer.service.technology.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

import static com.miyu.cloud.mcs.enums.DictConstants.*;

@Schema(description = "RPC 服务 - 工序信息")
@Data
public class ProcedureRespDTO {

    @Schema(description = "工序id")
    private String id;

    @Schema(description = "工序序号")
    private String procedureNum;

    @Schema(description = "工序名称(加工路线)")
    private String procedureName;

    @Schema(description = "是否专检")
    private Integer isInspect;

    @Schema(description = "工序关重属性")
    private Integer procedureProperty;

    @Schema(description = "是否外委")
    private Integer isOut;

    @Schema(description = "准备工时")
    private Integer preparationTime;

    @Schema(description = "加工工时")
    private Integer processingTime;

    @Schema(description = "工作说明")
    private String description;

    @Schema(description = "工作说明-预览")
    private String descriptionPreview;

    @Schema(description = "工步列表")
    private List<StepRespDTO> stepList;

    @Schema(description = "制造资源列表")
    private List<ProcedureDetailRespDTO> resourceList;

    @Schema(description = "工序草图列表")
    private List<ProcedureFileRespDTO> fileList;

    public Integer getInspectStatus() {
        int status = isInspect == 0 ? MCS_DETAIL_INSPECT_STATUS_SELF : MCS_DETAIL_INSPECT_STATUS_SPECIAL;
        if (procedureName.contains("装夹")) status = MCS_DETAIL_INSPECT_STATUS_FINISH;
        if (isOut == 1) status = MCS_DETAIL_INSPECT_STATUS_FINISH;
        return status;
    }

    public static boolean isIgnoreProcedure(ProcedureRespDTO procedureRespDTO) {
        return ("05".equals(procedureRespDTO.getProcedureNum()) && procedureRespDTO.getProcedureName().contains("来料"))
                || procedureRespDTO.getProcedureName().contains("入库");
    }
}
