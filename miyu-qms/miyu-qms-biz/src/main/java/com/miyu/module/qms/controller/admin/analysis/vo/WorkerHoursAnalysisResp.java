package com.miyu.module.qms.controller.admin.analysis.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.ToString;

import java.math.BigDecimal;


@Schema(description = "管理后台 - 分析 VO")
@Data
@ToString(callSuper = true)
public class WorkerHoursAnalysisResp {

    @Schema(description = "检测数量")
    private Integer checkNumber;
    @Schema(description = "工时")
    private BigDecimal workerHoursNumber;
    @Schema(description = "姓名")
    private String name;
    @Schema(description = "效率")
    private BigDecimal rate;

}
