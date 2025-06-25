package com.miyu.module.qms.controller.admin.inspectionsheetscheme.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotEmpty;


@Schema(description = "管理后台 - 生产终端任务检验 Request VO")
@Data
public class InspectionSchemeReqVO {
    @Schema(description = "检测人员ID")
    private String assignmentId;

    @Schema(description = "部门ID")
    private String deptId;
}
