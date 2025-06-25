package com.miyu.module.mcc.controller.admin.encodingrule.vo;

import lombok.*;
import java.util.*;
import io.swagger.v3.oas.annotations.media.Schema;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;

import static cn.iocoder.yudao.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

@Schema(description = "管理后台 - 编码规则配置分页 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class EncodingRulePageReqVO extends PageParam {

    @Schema(description = "创建时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] createTime;

    @Schema(description = "名称", example = "张三")
    private String name;

    @Schema(description = "编码分类", example = "28954")
    private String classificationId;

    @Schema(description = "启用状态  1启用 0未启用", example = "2")
    private Integer status;

    @Schema(description = "总位数")
    private Integer totalBitNumber;


    @Schema(description = "所属分类", example = "9973")
    private String materialTypeId;

    @Schema(description = "类型", example = "9973")
    private Integer encodingRuleType;

    private Integer autoRelease;
}