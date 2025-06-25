package com.miyu.module.pdm.controller.admin.processDetailTask.vo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema(description = "管理后台 - 项目零件目录 Response VO")
@Data
@ExcelIgnoreUnannotated
public class ProjPartBomTreeRespVO {

    @Schema(description = "id", requiredMode = Schema.RequiredMode.REQUIRED, example = "6902")
    private String id;

    @Schema(description = "类型(1零件/2工艺方案/3工序)")
    private String type;

    @Schema(description = "类型名称(零件/工艺方案/工序)")
    private String typeName;

    @Schema(description = "上级节点")
    private String parentId;

    @Schema(description = "节点名称")
    private String name;

    @Schema(description = "零部件版本ID")
    private String partVersionId;

    @Schema(description = "工艺规程版本id")
    private String processVersionId;

    @Schema(description = "状态")
    private String status;

    @Schema(description = "负责人id")
    private String reviewedBy;

    @Schema(description = "负责人")
    private String reviewer;

    @Schema(description = "编制截止日期")
    private String deadline;

    @Schema(description = "工艺详细任务id")
    private String taskId;

    @Schema(description = "流程审批实例id")
    private String processInstanceId;

}
