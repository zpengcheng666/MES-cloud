package com.miyu.module.qms.controller.admin.analysis.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.ToString;

import java.math.BigDecimal;


@Schema(description = "管理后台 - 分析 VO")
@Data
@ToString(callSuper = true)
public class WorkerAnalysisResp {

    @Schema(description = "检测数量")
    private Integer checkNumber;
    @Schema(description = "不合格数量")
    private Integer unqualifiedNumber;
    @Schema(description = "合格数量")
    private Integer qualifiedNumber;
    @Schema(description = "姓名")
    private String name;
    @Schema(description = "合格率")
    private BigDecimal rate;

}
