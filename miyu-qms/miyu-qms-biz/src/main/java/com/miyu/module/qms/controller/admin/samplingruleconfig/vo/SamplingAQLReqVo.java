package com.miyu.module.qms.controller.admin.samplingruleconfig.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Schema(description = "管理后台 - 抽样规则（检验抽样方案 新增 Request VO")
@Data
public class SamplingAQLReqVo {



    @Schema(description = "接收质量限（AQL）", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "接收质量限（AQL）不能为空")
    private BigDecimal acceptanceQualityLimit;

    @Schema(description = "接收数（Ac）", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "接收数（Ac）不能为空")
    private Integer acceptNum;

    @Schema(description = "拒收数（Re）", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "拒收数（Re）不能为空")
    private Integer rejectionNum;
}
