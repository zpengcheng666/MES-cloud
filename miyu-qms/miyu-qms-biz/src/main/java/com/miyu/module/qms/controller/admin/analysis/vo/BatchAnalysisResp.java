package com.miyu.module.qms.controller.admin.analysis.vo;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.ToString;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;


@Schema(description = "管理后台 - 分析 VO")
@Data
@ToString(callSuper = true)
public class BatchAnalysisResp {


    @Schema(description = "批次号")
    private String name;
    @Schema(description = "批次数量")
    private Integer batchCount;
    @Schema(description = "批次合格率")
    private BigDecimal passRate;
}
