package com.miyu.cloud.dms.controller.admin.maintenancerecord.vo;

import cn.iocoder.yudao.framework.common.pojo.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

import static cn.iocoder.yudao.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

@Schema(description = "管理后台 - 设备保养维护记录分页 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class MaintenanceRecordPageReqVO extends PageParam {

    @Schema(description = "用户角色")
    private String[] roles;

    @Schema(description = "计划编码")
    private String code;

    @Schema(description = "保养维护记录状态")
    private Integer recordStatus;

    @Schema(description = "设备")
    private String device;

    @Schema(description = "是否为关键设备")
    private Integer criticalDevice;

    @Schema(description = "是否超期停机", example = "2")
    private Integer expirationShutdown;

    @Schema(description = "超期时间")
    private Integer expirationTime;

    @Schema(description = "保养类型", example = "2")
    private Integer type;

    @Schema(description = "完成状态", example = "2")
    private Integer status;

    @Schema(description = "备注")
    private String remarks;

    @Schema(description = "保养内容")
    private String content;

    @Schema(description = "保养人")
    private String maintenanceBy;

    @Schema(description = "开始维护时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] startTime;

    @Schema(description = "结束维护时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] endTime;

    @Schema(description = "创建时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] createTime;

}
