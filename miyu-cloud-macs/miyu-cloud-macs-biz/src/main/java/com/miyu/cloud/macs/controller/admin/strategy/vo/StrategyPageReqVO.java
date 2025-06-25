package com.miyu.cloud.macs.controller.admin.strategy.vo;

import lombok.*;
import java.util.*;
import io.swagger.v3.oas.annotations.media.Schema;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;

import static cn.iocoder.yudao.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

@Schema(description = "管理后台 - 策略分页 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class StrategyPageReqVO extends PageParam {

    @Schema(description = "编码")
    private String code;

    @Schema(description = "名称", example = "李四")
    private String name;

    @Schema(description = "描述", example = "随便")
    private String description;

    @Schema(description = "状态（1启用 0不启用）", example = "1")
    private Integer status;

    @Schema(description = "创建人")
    private String createBy;

    @Schema(description = "创建时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] createTime;

    @Schema(description = "更新人")
    private String updateBy;

}