package com.miyu.module.pdm.controller.admin.processTask.vo;

import cn.iocoder.yudao.framework.excel.core.annotations.DictFormat;
import cn.iocoder.yudao.module.system.enums.DictTypeConstants;
import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema(description = "管理后台 - 项目零件目录 Response VO")
@Data
@ExcelIgnoreUnannotated
public class ProjPartBomTreeRespVO {

    @Schema(description = "id", requiredMode = Schema.RequiredMode.REQUIRED, example = "6902")
    private String id;

    @Schema(description = "类型(0根节点1零件2工艺方案3工序4工步)")
    private Integer type;

    @Schema(description = "上级节点")
    private String parentId;

    @Schema(description = "节点名称")
    private String name;

    @Schema(description = "零部件版本ID")
    private String partVersionId;

    @Schema(description = "工艺规程版本id")
    private String processVersionId;

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

    @Schema(description = "项目编号")
    private String projectCode;

    @Schema(description = "加工状态")
    private String processCondition;
}
