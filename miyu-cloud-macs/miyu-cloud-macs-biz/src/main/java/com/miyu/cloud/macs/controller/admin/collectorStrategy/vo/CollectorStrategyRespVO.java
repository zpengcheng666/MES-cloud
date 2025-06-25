package com.miyu.cloud.macs.controller.admin.collectorStrategy.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import java.util.*;
import com.alibaba.excel.annotation.*;

@Schema(description = "管理后台 - 设备策略 Response VO")
@Data
@ExcelIgnoreUnannotated
public class CollectorStrategyRespVO {

    @Schema(description = "主键id", requiredMode = Schema.RequiredMode.REQUIRED, example = "12359")
    @ExcelProperty("主键id")
    private String id;

    @Schema(description = "设备id", example = "23925")
    @ExcelProperty("设备id")
    private String collectorId;

    @Schema(description = "策略id", example = "8897")
    @ExcelProperty("策略id")
    private String strategyId;

}