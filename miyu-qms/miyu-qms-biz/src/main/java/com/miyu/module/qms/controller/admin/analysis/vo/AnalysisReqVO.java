package com.miyu.module.qms.controller.admin.analysis.vo;

import cn.iocoder.yudao.framework.common.pojo.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.List;

import static cn.iocoder.yudao.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

@Schema(description = "管理后台 - 分析 VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class AnalysisReqVO extends PageParam {

    @Schema(description = "查询时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] queryTime;

    @Schema(description = "物料类型")
    private String materialConfigId;

    @Schema(description = "批次号")
    private String batchNumber;

    @Schema(description = "方案类型 来料检验  过程检验 完工检验")
    private List<Integer> schemeType;
}
