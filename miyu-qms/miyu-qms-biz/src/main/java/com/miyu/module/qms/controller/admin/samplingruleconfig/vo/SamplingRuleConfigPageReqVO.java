package com.miyu.module.qms.controller.admin.samplingruleconfig.vo;

import lombok.*;
import java.util.*;
import io.swagger.v3.oas.annotations.media.Schema;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import java.math.BigDecimal;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;

import static cn.iocoder.yudao.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

@Schema(description = "管理后台 - 抽样规则（检验抽样方案）分页 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class SamplingRuleConfigPageReqVO extends PageParam {

    @Schema(description = "样本量字码")
    private String sampleSizeCode;

    @Schema(description = "类型  1正常检查2加严检查3放宽检查", example = "2")
    private Integer samplingRuleType;

    @Schema(description = "接收质量限（AQL）")
    private BigDecimal acceptanceQualityLimit;
    @Schema(description = "抽样标准ID", example = "23748")
    private String samplingStandardId;
}