package com.miyu.module.pdm.controller.admin.processPlanDetail.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema(description = "PDM - 工艺修改单 Request VO")
@Data
public class ProcessChangeReqVO {

    @Schema(description = "id", example = "1")
    private String id;

    @Schema(description = "项目编号" )
    private String projectCode;

    @Schema(description = "工艺规程版本id" )
    private String processVersionId;

    @Schema(description = "更改单号" )
    private String changeCode;

    @Schema(description = "更改原因" )
    private String changeReason;

    @Schema(description = "更改内容" )
    private String changeContent;

    @Schema(description = "在制品处理意见" )
    private String workingOpinions;

    @Schema(description = "更改单状态(0工作中1审批中2审批失败3已定版)" )
    private Integer status;

    @Schema(description = "流程实例的编号" )
    private String processInstanceId;

    @Schema(description = "流程审批状态" )
    private Integer approvalStatus;

}
