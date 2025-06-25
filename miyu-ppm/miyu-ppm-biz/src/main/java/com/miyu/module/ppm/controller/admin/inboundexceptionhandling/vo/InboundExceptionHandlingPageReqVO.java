package com.miyu.module.ppm.controller.admin.inboundexceptionhandling.vo;

import lombok.*;
import java.util.*;
import io.swagger.v3.oas.annotations.media.Schema;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import java.math.BigDecimal;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;

import static cn.iocoder.yudao.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

@Schema(description = "管理后台 - 入库异常处理分页 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class InboundExceptionHandlingPageReqVO extends PageParam {

    @Schema(description = "入库单号")
    private String no;

    @Schema(description = "创建日期")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] createTime;

    @Schema(description = "状态  0待处理  1已处理", example = "1")
    private Integer status;

    @Schema(description = "类型 1采购收货 2外协退货 3原材料入库 4 销售退货", example = "1")
    private Integer consignmentType;

    @Schema(description = "处理结果  1入库 2退货", example = "1")
    private Integer rusultType;

    @Schema(description = "异常类型 1来货不足 2收货收多 ", example = "1")
    private Integer exceptionType;

    @Schema(description = "合同ID", example = "5401")
    private String contractId;

    @Schema(description = "项目ID", example = "32229")
    private String projectId;

    @Schema(description = "公司ID", example = "18492")
    private String companyId;

}