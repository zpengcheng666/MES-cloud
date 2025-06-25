package com.miyu.module.qms.controller.admin.samplingruleconfig.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.List;

@Schema(description = "管理后台 - 抽样规则（检验抽样方案）新增/修改 Request VO")
@Data
public class SamplingRuleConfigSaveBatchReqVO {


    @Schema(description = "主键", requiredMode = Schema.RequiredMode.REQUIRED, example = "21112")
    private String id;

    @Schema(description = "样本量字码", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotEmpty(message = "样本量字码不能为空")
    private String sampleSizeCode;

    @Schema(description = "抽取样本数量", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "抽取样本数量不能为空")
    private Integer sampleSize;

    @Schema(description = "类型  1正常检查2加严检查3放宽检查", requiredMode = Schema.RequiredMode.REQUIRED, example = "2")
    @NotNull(message = "类型  1正常检查2加严检查3放宽检查不能为空")
    private Integer samplingRuleType;

    @Schema(description = "抽样标准ID", example = "23748")
    private String samplingStandardId;


    @NotNull(message = "请填写AQL值")
    private List<SamplingAQLReqVo> aqlReqVoList;



}
