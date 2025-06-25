package com.miyu.cloud.dms.controller.admin.failurerecord.vo;

import cn.iocoder.yudao.framework.common.pojo.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.time.LocalDateTime;

@Schema(description = "管理后台 - 异常记录分页 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class FailureRecordPageReqVO extends PageParam {

    @Schema(description = "设备")
    private String device;

    @Schema(description = "异常编码")
    private String code;

    @Schema(description = "故障状态")
    private String faultState;

    @Schema(description = "故障时间")
    private LocalDateTime[] faultTime;

    @Schema(description = "创建时间")
    private LocalDateTime[] createTime;
}
