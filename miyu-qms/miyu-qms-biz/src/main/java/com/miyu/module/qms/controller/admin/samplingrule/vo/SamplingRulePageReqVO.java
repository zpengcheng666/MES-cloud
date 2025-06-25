package com.miyu.module.qms.controller.admin.samplingrule.vo;

import lombok.*;
import java.util.*;
import io.swagger.v3.oas.annotations.media.Schema;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;

import static cn.iocoder.yudao.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

@Schema(description = "管理后台 - 抽样规则分页 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class SamplingRulePageReqVO extends PageParam {

    @Schema(description = "创建时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] createTime;

    @Schema(description = "抽样标准ID", example = "32034")
    private String samplingStandardId;

    @Schema(description = "样本数字码")
    private String sampleSizeCode;

    @Schema(description = "批量下限值（N）")
    private Integer minValue;

    @Schema(description = "批量上限值（N）")
    private Integer maxValue;

    @Schema(description = "检验水平类型", example = "2")
    private Integer inspectionLevelType;

}