package com.miyu.cloud.mcs.dto.productionProcess;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

/**
 * 工序
 */
@Data
public class McsPlanProcessDTO {

    @Schema(description = "工艺信息")
    private McsPlanPartDTO part;

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

    @Schema(description = "工作说明-预览(html)")
    private String descriptionPreview;

    @Schema(description = "工步列表")
    private List<McsPlanStepDTO> stepList;

    @Schema(description = "制造资源列表")
    private List<McsPlanResourcesDTO> resourceList;

    @Schema(description = "工序草图列表")
    private List<ProcedureFileRespDTO> fileList;
}
