package com.miyu.module.qms.controller.admin.inspectionsheetscheme.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;


@Schema(description = "管理后台 - 生产终端任务检验 Request VO")
@Data
public class InspectionSchemeTerminalReqVO {
    @Schema(description = "检测人员ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "1234")
    @NotEmpty(message = "检验人员不能为空")
    private String assignmentId;

    @Schema(description = "工序ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "1234")
//    @NotEmpty(message = "工序不能为空")
    private String processId;

    @Schema(description = "条码")
    private String barCode;

    @Schema(description = "部门ID")
    private String deptId;
}
