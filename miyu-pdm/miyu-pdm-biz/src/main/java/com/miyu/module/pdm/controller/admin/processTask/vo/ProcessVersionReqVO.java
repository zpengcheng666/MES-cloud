package com.miyu.module.pdm.controller.admin.processTask.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.ToString;

@Schema(description = "管理后台 - 工艺任务项目列表 Request VO")
@Data
@ToString(callSuper = true)
public class ProcessVersionReqVO {
    @TableId(type = IdType.ASSIGN_ID)
    @Schema(description = "id", example = "20041")
    private String id;

    @Schema(description = "工艺规程源id", example = "20041")
    private String sourceId;

    @Schema(description = "零件工艺规程id", example = "20041")
    private String processId;

    @Schema(description = "零件工艺规程版本号", example = "20041")
    private String processVersion;

    @Schema(description = "零件工艺规程名称", example = "20041")
    private String processName;

    @Schema(description = "工作说明", example = "20041")
    private String description;

    @Schema(description = "设计更改单id", example = "20041")
    private String designChangeId;

    @Schema(description = "关重属性", example = "20041")
    private Integer property;

    @Schema(description = "工艺更改单id", example = "20041")
    private String processChangeId;

    @Schema(description = "工艺规程状态", example = "20041")
    private Integer status;

    @Schema(description = "是否有效", example = "20041")
    private Integer isValid;

    @Schema(description = "流程实例的编号", example = "20041")
    private String processInstanceId;

    @Schema(description = "流程审批状态", example = "20041")
    private String approvalStatus;
}
