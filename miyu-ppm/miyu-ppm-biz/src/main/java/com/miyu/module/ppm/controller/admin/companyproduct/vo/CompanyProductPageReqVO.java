package com.miyu.module.ppm.controller.admin.companyproduct.vo;

import com.mzt.logapi.starter.annotation.DiffLogField;
import lombok.*;
import java.util.*;
import io.swagger.v3.oas.annotations.media.Schema;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import java.math.BigDecimal;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.DecimalMin;
import java.time.LocalDateTime;

import static cn.iocoder.yudao.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

@Schema(description = "管理后台 - 企业产品表，用于销售和采购分页 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class CompanyProductPageReqVO extends PageParam {

    @Schema(description = "企业ID", example = "18942")
    private String companyId;

    @Schema(description = "material表ID", example = "7721")
    private String materialId;

    @Schema(description = "初始单价", example = "8667")
    @DiffLogField(name = "初始单价")
    @DecimalMin(value = "0", inclusive = false, message = "初始单价必须大于零")
    private BigDecimal initPrice;

    @Schema(description = "初始税率", example = "1")
    private Integer initTax;

    @Schema(description = "供货周期")
    @DiffLogField(name = "供货周期")
    @DecimalMin(value = "0", inclusive = false, message = "供货周期必须大于零")
    private Integer leadTime;

    @Schema(description = "是否免检")
    private Integer qualityCheck;

    @Schema(description = "创建时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] createTime;

    @Schema(description = "创建IP")
    private String creationIp;

    @Schema(description = "更新ip")
    private String updatedIp;

    private String productName;
}