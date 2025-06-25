package com.miyu.cloud.dms.controller.admin.sparepart.vo;

import lombok.*;
import java.util.*;
import io.swagger.v3.oas.annotations.media.Schema;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;

import static cn.iocoder.yudao.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

@Schema(description = "管理后台 - 备品/备件分页 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class SparePartPageReqVO extends PageParam {

    @Schema(description = "id", example = "14340")
    private String id;

    @Schema(description = "备件编码")
    private String code;

    @Schema(description = "备件名称", example = "芋艿")
    private String name;

    @Schema(description = "备件数量")
    private Integer number;

    @Schema(description = "备件类型", example = "2")
    private String type;

    @Schema(description = "备注", example = "你猜")
    private String remark;

    @Schema(description = "创建时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] createTime;

}