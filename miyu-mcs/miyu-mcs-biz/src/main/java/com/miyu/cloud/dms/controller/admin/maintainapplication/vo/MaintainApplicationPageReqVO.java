package com.miyu.cloud.dms.controller.admin.maintainapplication.vo;

import cn.iocoder.yudao.framework.common.pojo.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

import static cn.iocoder.yudao.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

@Schema(description = "管理后台 - 设备维修申请分页 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class MaintainApplicationPageReqVO extends PageParam {

    @Schema(description = "设备id")
    private String device;

    @Schema(description = "设备编码")
    private String code;

    @Schema(description = "设备名称")
    private String name;

    @Schema(description = "生产单元编号")
    private String processingUnitNumber;

    @Schema(description = "设备型号")
    private String model;

    @Schema(description = "关键设备")
    private Integer important;

    @Schema(description = "维修类型")
    private Integer type;

    @Schema(description = "故障信息描述")
    private String describe1;

    @Schema(description = "期望修复时间")
    private Integer duration;

    @Schema(description = "申请状态")
    private Integer status;

    @Schema(description = "流程实例编号")
    private String processInstanceId;

    @Schema(description = "申请人")
    private String applicant;

    @Schema(description = "申请时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] applicationTime;

    @Schema(description = "创建时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] createTime;

}
