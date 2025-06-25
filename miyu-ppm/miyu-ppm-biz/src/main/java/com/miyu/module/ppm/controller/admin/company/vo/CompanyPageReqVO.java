package com.miyu.module.ppm.controller.admin.company.vo;

import lombok.*;
import java.util.*;
import io.swagger.v3.oas.annotations.media.Schema;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import java.math.BigDecimal;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;

import static cn.iocoder.yudao.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

@Schema(description = "管理后台 - 企业基本信息分页 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class CompanyPageReqVO extends PageParam {

    @Schema(description = "名称", example = "张三")
    private String name;

    @Schema(description = "状态", example = "2")
    private Integer companyStatus;

    @Schema(description = "类型", example = "1")
    private Integer type;

    @Schema(description = "行业分类，参见国民经济行业分类")
    private Integer industryClassification;

    @Schema(description = "供求类型", example = "1,2")
    private Set<String> supplyType;
}