package com.miyu.module.qms.controller.admin.analysis.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.ToString;

import java.math.BigDecimal;

@Schema(description = "管理后台 - 分析 VO")
@Data
@ToString(callSuper = true)
public class ScrapAndRepairResp {
    @Schema(description = "检测数量")
    private Integer checkNumber;
    @Schema(description = "报废数量")
    private Integer scrapNumber;
    @Schema(description = "返修数量")
    private Integer repairNumber;
}
