package com.miyu.module.pdm.api.projectAssessment.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema(description = "RPC 服务 - 零件相关")
@Data
public class ProjPartBomRespDTO {
    private String id;

    private String partVersionId;

    private String partNumber;

    private String partName;

    private String partVersion;

    private String rootProductId;

    private String productNumber;

    private String customizedIndex;

    private String reviewedBy;

    private String reviewer;

    private String deadline;

    private Integer status;

    private String taskId;

    private String processInstanceId;

    private String projectCode;

    private Integer isPass;

    private String reason;
}
