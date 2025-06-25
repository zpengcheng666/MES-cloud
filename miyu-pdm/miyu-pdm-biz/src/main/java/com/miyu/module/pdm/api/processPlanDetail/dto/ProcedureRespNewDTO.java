package com.miyu.module.pdm.api.processPlanDetail.dto;

import cn.iocoder.yudao.framework.mybatis.core.type.JsonLongSetTypeHandler;
import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;
import java.util.Set;

@Schema(description = "RPC 服务 - 工序信息")
@Data
public class ProcedureRespNewDTO {

    @Schema(description = "工序id")
    private String id;

    @Schema(description = "工序序号")
    private String procedureNum;

    @Schema(description = "工序名称(加工路线)")
    private String procedureName;

    @Schema(description = "是否专检")
    private String isInspect;

    @Schema(description = "工序关重属性")
    private Integer procedureProperty;

    @Schema(description = "是否外委")
    private String isOut;

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
}
