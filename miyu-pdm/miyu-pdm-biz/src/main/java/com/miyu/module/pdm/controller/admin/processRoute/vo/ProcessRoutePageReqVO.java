package com.miyu.module.pdm.controller.admin.processRoute.vo;

import cn.iocoder.yudao.framework.common.pojo.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

import static cn.iocoder.yudao.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;
@Schema(description = "pdm - 加工路线分页 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
public class ProcessRoutePageReqVO extends PageParam{

        @Schema(description = "加工路线名，模糊匹配", example = "C")
        private String name;

        @Schema(description = "加工路线编号", example = "1")
        private String Id;

        @Schema(description = "创建时间", example = "[2022-07-01 00:00:00,2022-07-01 23:59:59]")
        @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
        private LocalDateTime[] createTime;
}
