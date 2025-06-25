package com.miyu.module.pdm.api.projectPlan.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema(description = "RPC 服务 - 工艺编程数据列表")
@Data
public class ProjPartBomTreeRespDTO {
    @Schema(description = "id", requiredMode = Schema.RequiredMode.REQUIRED, example = "6902")
    private String id;

    @Schema(description = "类型(0根节点1零件节点2工艺方案节点)")
    private Integer type;

    @Schema(description = "上级节点")
    private String parentId;

    @Schema(description = "节点名称")
    private String name;

    @Schema(description = "零部件版本ID")
    private String partVersionId;

    @Schema(description = "零件图号")
    private String partNumber;

    @Schema(description = "状态")
    private String status;

    @Schema(description = "负责人id")
    private String reviewedBy;

    @Schema(description = "工艺任务id")
    private String taskId;

    @Schema(description = "流程审批实例id")
    private String processInstanceId;

    @Schema(description = "工艺规程来源(1新建 2已定版 3升版)")
    private Integer source;

    @Schema(description = "")
    private String processVersionId;
}
