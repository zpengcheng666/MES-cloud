package com.miyu.cloud.macs.controller.admin.visitorRegion.vo;

import lombok.*;
import io.swagger.v3.oas.annotations.media.Schema;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;

import static cn.iocoder.yudao.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

@Schema(description = "管理后台 - 访客区域权限分页 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class VisitorRegionPageReqVO extends PageParam {

    @Schema(description = "访客id", example = "6699")
    private String visitorId;

    @Schema(description = "区域id", example = "6759")
    private String regionId;

    @Schema(description = "访客申请id", example = "6146")
    private String applicationId;

    @Schema(description = "生效日期")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] effectiveDate;

    @Schema(description = "失效日期")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] invalidDate;

}