package com.miyu.cloud.dms.controller.admin.devicetype.vo;

import cn.iocoder.yudao.framework.common.pojo.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

import static cn.iocoder.yudao.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

@Schema(description = "管理后台 - 设备/工位类型分页 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class DeviceTypePageReqVO extends PageParam {

    @Schema(description = "id", example = "15312")
    private String id;

    @Schema(description = "类型编号", example = "HSG-9387")
    private String code;

    @Schema(description = "类型名称", example = "4号控制器")
    private String name;

    @Schema(description = "设备/工位")
    private Integer type;

    @Schema(description = "是否启用")
    private Integer enable;

    @Schema(description = "规格型号")
    private String specification;

    @Schema(description = "生产厂家")
    private String manufacturer;

    @Schema(description = "产地")
    private String countryRegion;

    @Schema(description = "厂家联系人")
    private String contacts;

    @Schema(description = "厂家联系电话")
    private String contactPhone;

    @Schema(description = "备注")
    private String remark;

    @Schema(description = "创建者")
    private String creator;

    @Schema(description = "创建时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] createTime;

    @Schema(description = "更新者")
    private String updater;

    @Schema(description = "更新时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] updateTime;
}