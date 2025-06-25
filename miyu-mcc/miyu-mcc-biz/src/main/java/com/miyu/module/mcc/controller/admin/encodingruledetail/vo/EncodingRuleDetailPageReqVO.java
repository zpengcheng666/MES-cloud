package com.miyu.module.mcc.controller.admin.encodingruledetail.vo;

import lombok.*;
import java.util.*;
import io.swagger.v3.oas.annotations.media.Schema;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;

import static cn.iocoder.yudao.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

@Schema(description = "管理后台 - 编码规则配置详情分页 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class EncodingRuleDetailPageReqVO extends PageParam {

    @Schema(description = "创建时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] createTime;

    @Schema(description = "类型1机构 2编码分类 3类别4源码5连接符6数字流水号7字母流水号8年份9月份10日期", example = "2")
    private Integer type;

    @Schema(description = "编码规则表ID", example = "23792")
    private String encodingRuleId;

    @Schema(description = "排序")
    private Integer sort;

    @Schema(description = "位数")
    private Integer bitNumber;

    @Schema(description = "描述名", example = "赵六")
    private String name;

    @Schema(description = "默认值")
    private String defalutValue;

    @Schema(description = "规则 1固定  2自定义 3自生成", example = "1")
    private Integer ruleType;

    @Schema(description = "编码属性   （当自定义的时候可以选择属性方便传值）")
    private String encodingAttribute;

    @Schema(description = "来源规则Id", example = "9973")
    private String sourceRuleId;

    @Schema(description = "所属分类", example = "9973")
    private String materialTypeId;
}