package com.miyu.cloud.macs.controller.admin.region.vo;

import lombok.*;
import java.util.*;
import io.swagger.v3.oas.annotations.media.Schema;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import java.time.LocalDateTime;
import org.springframework.format.annotation.DateTimeFormat;

import static cn.iocoder.yudao.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

@Schema(description = "管理后台 - 区域列表 Request VO")
@Data
public class RegionListReqVO {

    @Schema(description = "区域编码")
    private String code;

    @Schema(description = "区域名称", example = "李四")
    private String name;

    @Schema(description = "公开状态(0未公开,1公开)", example = "2")
    private Boolean publicStatus;

    @Schema(description = "描述", example = "你说的对")
    private String description;

    @Schema(description = "上级id", example = "20787")
    private String parentId;

    @Schema(description = "创建人")
    private String createBy;

    @Schema(description = "创建时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] createTime;

    @Schema(description = "更新人")
    private String updateBy;

}