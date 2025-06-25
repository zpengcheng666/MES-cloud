package com.miyu.module.tms.controller.admin.toolparamtemplatedetail.vo;

import lombok.*;
import java.util.*;
import io.swagger.v3.oas.annotations.media.Schema;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;

import static cn.iocoder.yudao.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

@Schema(description = "管理后台 - 参数模版详情分页 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class ToolParamTemplateDetailPageReqVO extends PageParam {

    @Schema(description = "创建时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] createTime;

    @Schema(description = "参数模板主表ID", example = "23730")
    private String paramTemplateId;

    @Schema(description = "参数名称", example = "李四")
    private String name;

    @Schema(description = "参数缩写")
    private String abbr;

    @Schema(description = "计量单位")
    private String unit;

    @Schema(description = "排序")
    private Integer sort;

    @Schema(description = "分类 1 几何参数  2 切削参数", example = "2")
    private Boolean type;

    @Schema(description = "是否必填  1 否 2 是")
    private Boolean required;

    @Schema(description = "默认参数值（下拉框时）")
    private String defaultValue;

}