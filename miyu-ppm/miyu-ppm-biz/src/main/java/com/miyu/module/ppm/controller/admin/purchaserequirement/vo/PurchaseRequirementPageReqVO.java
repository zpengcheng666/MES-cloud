package com.miyu.module.ppm.controller.admin.purchaserequirement.vo;

import lombok.*;
import io.swagger.v3.oas.annotations.media.Schema;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;

import static cn.iocoder.yudao.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

@Schema(description = "管理后台 - 采购申请主分页 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class PurchaseRequirementPageReqVO extends PageParam {

    @Schema(description = "采购类型", example = "2")
    private Integer type;

    @Schema(description = "申请人")
    private String applicant;

    @Schema(description = "申请部门")
    private String applicationDepartment;

    @Schema(description = "申请日期")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] applicationDate;

    @Schema(description = "申请理由", example = "不香")
    private String applicationReason;

    @Schema(description = "创建时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] createTime;

    @Schema(description = "是否有效")
    private Integer isValid;

    @Schema(description = "审批状态")
    private Integer status;

    @Schema(description = "创建IP")
    private String creationIp;

    @Schema(description = "更新ip")
    private String updatedIp;

    @Schema(description = "是否查当前部门数据")
    private String isCurrentDept;

}
