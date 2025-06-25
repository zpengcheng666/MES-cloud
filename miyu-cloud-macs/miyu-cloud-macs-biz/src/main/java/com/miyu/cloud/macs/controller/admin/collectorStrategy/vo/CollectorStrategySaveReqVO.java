package com.miyu.cloud.macs.controller.admin.collectorStrategy.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import javax.validation.constraints.*;

@Schema(description = "管理后台 - 设备策略新增/修改 Request VO")
@Data
public class CollectorStrategySaveReqVO {

    @Schema(description = "主键id", requiredMode = Schema.RequiredMode.REQUIRED, example = "12359")
    private String id;

    @Schema(description = "设备id", example = "23925")
    private String collectorId;

    @Schema(description = "策略id", example = "8897")
    private String strategyId;

}