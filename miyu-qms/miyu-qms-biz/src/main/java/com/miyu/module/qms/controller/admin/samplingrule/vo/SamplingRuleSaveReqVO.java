package com.miyu.module.qms.controller.admin.samplingrule.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import javax.validation.constraints.*;

@Schema(description = "管理后台 - 抽样规则新增/修改 Request VO")
@Data
public class SamplingRuleSaveReqVO {

    @Schema(description = "主键", requiredMode = Schema.RequiredMode.REQUIRED, example = "4339")
    private String id;

    @Schema(description = "抽样标准ID", example = "32034")
    private String samplingStandardId;

    @Schema(description = "样本数字码", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotEmpty(message = "样本数字码不能为空")
    private String sampleSizeCode;

    @Schema(description = "批量下限值（N）")
    private Integer minValue;

    @Schema(description = "批量上限值（N）")
    private Integer maxValue;

    @Schema(description = "检验水平类型", example = "2")
    private Integer inspectionLevelType;

}