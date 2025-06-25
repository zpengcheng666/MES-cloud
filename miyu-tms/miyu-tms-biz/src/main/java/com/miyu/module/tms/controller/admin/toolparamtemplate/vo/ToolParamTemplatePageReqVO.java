package com.miyu.module.tms.controller.admin.toolparamtemplate.vo;

import lombok.*;
import java.util.*;
import io.swagger.v3.oas.annotations.media.Schema;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;

import static cn.iocoder.yudao.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

@Schema(description = "管理后台 - 刀具参数模板分页 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class ToolParamTemplatePageReqVO extends PageParam {

    @Schema(description = "创建时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] createTime;

    @Schema(description = "模板名称", example = "张三")
    private String templateName;

    @Schema(description = "版本号， 每次更新递增")
    private Integer version;

    @Schema(description = "状态(0:失效 1:有效)", example = "2")
    private Boolean status;

    @Schema(description = "物料类别ID", example = "1")
    private String materialTypeId;

    @Schema(description = "刀具类型ID", example = "1")
    private String toolConfigId;

    @Schema(description = "物料编号",  example = "1")
    private String toolNumber;

    @Schema(description = "类码",  example = "1")
    private String toolTypeCode;

}
