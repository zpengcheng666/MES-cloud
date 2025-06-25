package com.miyu.module.qms.controller.admin.inspectionsheetsamplingrule.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.time.LocalDateTime;
import com.alibaba.excel.annotation.*;

@Schema(description = "管理后台 - 检验单抽样规则（检验抽样方案）关系 Response VO")
@Data
@ExcelIgnoreUnannotated
public class InspectionSheetSamplingRuleRespVO {

    @Schema(description = "主键", requiredMode = Schema.RequiredMode.REQUIRED, example = "485")
    @ExcelProperty("主键")
    private String id;

    @Schema(description = "创建时间", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("创建时间")
    private LocalDateTime createTime;

    @Schema(description = "检测任务ID", example = "32464")
    @ExcelProperty("检测任务ID")
    private String inspectionSheetSchemeId;

    @Schema(description = "检测项目ID", example = "21674")
    @ExcelProperty("检测项目ID")
    private String inspectionSchemeItemId;

    @Schema(description = "抽样方案ID", example = "23434")
    @ExcelProperty("抽样方案ID")
    private String samplingRuleConfigId;

    @Schema(description = "抽样标准ID", example = "1128")
    @ExcelProperty("抽样标准ID")
    private String samplingStandardId;

}