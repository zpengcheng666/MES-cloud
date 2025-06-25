package com.miyu.cloud.mcs.restServer.service.technology.dto;

import com.miyu.cloud.mcs.dto.productionProcess.CustomizedAttributeValRespDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Schema(description = "RPC 服务 - 工步信息")
@Data
public class StepRespDTO {

    @Schema(description = "工步id")
    private String id;

    @Schema(description = "工步序号")
    private String stepNum;

    @Schema(description = "工步名称")
    private String stepName;

    @Schema(description = "工步关重属性")
    private String stepProperty;

    @Schema(description = "工作说明")
    private String description;

    @Schema(description = "工作说明-预览")
    private String descriptionPreview;

    @Schema(description = "工时(min)")
    private Integer processingTime;

    @Schema(description = "工步类型")
    private Long categoryId;


    @Schema(description = "工步制造资源列表")
    private List<StepDetailRespDTO> resourceList;

    @Schema(description = "工步NC程序列表")
    private List<StepNcRespDTO> ncList;

    @Schema(description = "工步自定义属性列表")
    private List<CustomizedAttributeValRespDTO> attrValueList;

}
