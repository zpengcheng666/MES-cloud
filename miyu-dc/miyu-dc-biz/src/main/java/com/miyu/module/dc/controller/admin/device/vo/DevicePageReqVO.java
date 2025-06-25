package com.miyu.module.dc.controller.admin.device.vo;

import lombok.*;
import java.util.*;
import io.swagger.v3.oas.annotations.media.Schema;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;

import static cn.iocoder.yudao.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

@Schema(description = "管理后台 - 设备分页 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class DevicePageReqVO extends PageParam {

    @Schema(description = "设备名称", example = "王五")
    private String deviceId;

    @Schema(description = "设备类型", example = "王五")
    private String deviceTypeId;

    @Schema(description = "产品类型id", example = "30217")
    private String[] productTypeId;

    @Schema(description = "通信类型")
    private Integer commType;

    @Schema(description = "mqtt客户端id")
    private String deviceClientId;

    @Schema(description ="通讯url")
    private String deviceUrl;

    @Schema(description ="账号")
    private String username;

    @Schema(description ="密码")
    private String password;

    @Schema(description = "创建时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] createTime;

    private String topicId;

    private Integer deviceStatus;

}