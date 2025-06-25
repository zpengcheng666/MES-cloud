package com.miyu.module.tms.controller.admin.toolgroupdetail.vo;

import lombok.*;
import io.swagger.v3.oas.annotations.media.Schema;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;

import static cn.iocoder.yudao.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

@Schema(description = "管理后台 - 刀具组装分页 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class ToolGroupDetailPageReqVO extends PageParam {

    @Schema(description = "创建时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] createTime;

    @Schema(description = "刀具组装id", example = "25360")
    private String toolGroupId;

    @Schema(description = "成品刀具类型id", example = "25360")
    private String mainConfigId;

    @Schema(description = "刀位（非必填）")
    private Integer site;

    @Schema(description = "装配刀具类型id", example = "9521")
    private String accessoryConfigId;

    @Schema(description = "数量(仅限配件使用)", example = "15317")
    private Integer count;

}
