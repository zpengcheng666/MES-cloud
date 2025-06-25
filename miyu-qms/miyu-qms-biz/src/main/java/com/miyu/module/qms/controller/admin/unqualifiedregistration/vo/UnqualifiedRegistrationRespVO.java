package com.miyu.module.qms.controller.admin.unqualifiedregistration.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.time.LocalDateTime;

import com.alibaba.excel.annotation.*;

@Schema(description = "管理后台 - 不合格品登记 Response VO")
@Data
@ExcelIgnoreUnannotated
public class UnqualifiedRegistrationRespVO {

    @Schema(description = "主键", requiredMode = Schema.RequiredMode.REQUIRED, example = "30498")
    @ExcelProperty("主键")
    private String id;

    @Schema(description = "创建时间", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("创建时间")
    private LocalDateTime createTime;

    @Schema(description = "缺陷代码")
    @ExcelProperty("缺陷代码")
    private String defectiveCode;

    @Schema(description = "检验单方案任务ID", example = "13324")
    @ExcelProperty("检验单方案任务ID")
    private String inspectionSheetSchemeId;
}