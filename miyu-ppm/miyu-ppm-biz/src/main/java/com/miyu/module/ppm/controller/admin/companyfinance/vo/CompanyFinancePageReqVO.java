package com.miyu.module.ppm.controller.admin.companyfinance.vo;

import lombok.*;
import java.util.*;
import io.swagger.v3.oas.annotations.media.Schema;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;

import static cn.iocoder.yudao.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

@Schema(description = "管理后台 - 企业税务信息分页 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class CompanyFinancePageReqVO extends PageParam {

    @Schema(description = "公司ID", example = "7097")
    private String companyId;

    @Schema(description = "类型", example = "1")
    private Integer type;

    @Schema(description = "账号")
    private String accountNumber;

    @Schema(description = "银行")
    private String bank;

    @Schema(description = "地址")
    private String address;

    @Schema(description = "电话")
    private String telephone;

    @Schema(description = "联行号")
    private String bankAddress;

    @Schema(description = "创建时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] createTime;

    @Schema(description = "创建IP")
    private String creationIp;

    @Schema(description = "更新ip")
    private String updatedIp;

}