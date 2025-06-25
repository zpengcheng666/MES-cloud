package com.miyu.module.qms.controller.admin.inspectionschemeitem.vo;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.*;

import java.math.BigDecimal;
import java.util.*;
import io.swagger.v3.oas.annotations.media.Schema;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;

import static cn.iocoder.yudao.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

@Schema(description = "管理后台 - 检验方案检测项目详情分页 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class InspectionSchemeItemPageReqVO extends PageParam {

    @Schema(description = "创建时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] createTime;

    @Schema(description = "方案ID", example = "28444")
    private String inspectionSchemeId;

    @Schema(description = "检测项目ID", example = "1875")
    private String inspectionItemId;

    @Schema(description = "检测顺序")
    private Integer number;

    @Schema(description = "接收质量限")
    @ExcelProperty("接收质量限")
    private BigDecimal acceptanceQualityLimit;
}