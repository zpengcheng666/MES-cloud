package com.miyu.module.pdm.controller.admin.processTask.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema(description = "管理后台 - PDM 选中资源(设备、刀具、工装) Request VO")
@Data
public class ProcessSelectedReqVO {

    @Schema(description = "项目编号")
    private String projectCode;

    @Schema(description = "零部件版本ID")
    private String partVersionId;

    @Schema(description = "零件工艺规程版本id")
    private String processVersionId;

    @Schema(description = "工艺任务id")
    private String processTaskId;

    @Schema(description = "项目零件目录id")
    private String projPartBomId;

    @Schema(description = "工艺规程id")
    private String processId;

    @Schema(description = "工艺更改单id")
    private String processChangeId;
}
