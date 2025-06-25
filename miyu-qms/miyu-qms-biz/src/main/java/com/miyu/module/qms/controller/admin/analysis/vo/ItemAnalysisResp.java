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
public class ItemAnalysisResp {

    @Schema(description = "检测项Id")
    private String itemId;
    @Schema(description = "检测项名称")
    private String name;
    @Schema(description = "检测数量")
    private Integer itemNumber;
    @Schema(description = "不合格数量")
    private Integer unqualifiedNumber;
    @Schema(description = "合格率")
    private BigDecimal passRates;
}
