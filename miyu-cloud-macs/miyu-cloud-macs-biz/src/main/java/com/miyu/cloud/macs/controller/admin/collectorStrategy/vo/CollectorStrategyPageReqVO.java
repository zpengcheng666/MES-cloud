package com.miyu.cloud.macs.controller.admin.collectorStrategy.vo;

import lombok.*;
import java.util.*;
import io.swagger.v3.oas.annotations.media.Schema;
import cn.iocoder.yudao.framework.common.pojo.PageParam;

@Schema(description = "管理后台 - 设备策略分页 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class CollectorStrategyPageReqVO extends PageParam {

    @Schema(description = "设备id", example = "23925")
    private String collectorId;

    @Schema(description = "策略id", example = "8897")
    private String strategyId;

}