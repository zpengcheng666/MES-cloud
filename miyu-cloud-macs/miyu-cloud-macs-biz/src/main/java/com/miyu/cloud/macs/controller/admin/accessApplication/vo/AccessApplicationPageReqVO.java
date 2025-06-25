package com.miyu.cloud.macs.controller.admin.accessApplication.vo;

import lombok.*;
import io.swagger.v3.oas.annotations.media.Schema;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;

import static cn.iocoder.yudao.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

@Schema(description = "管理后台 - 通行申请分页 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class AccessApplicationPageReqVO extends PageParam {

    @Schema(description = "申请单号")
    private String applicationNumber;

    @Schema(description = "申请代理人")
    private String agent;

    @Schema(description = "公司/组织")
    private String organization;

    @Schema(description = "部门")
    private String department;

    @Schema(description = "申请原因/目的", example = "不香")
    private String reason;

    @Schema(description = "申请状态", example = "1")
    private String status;

    @Schema(description = "创建人")
    private String createBy;

    @Schema(description = "创建时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] createTime;

    @Schema(description = "更新人")
    private String updateBy;

}