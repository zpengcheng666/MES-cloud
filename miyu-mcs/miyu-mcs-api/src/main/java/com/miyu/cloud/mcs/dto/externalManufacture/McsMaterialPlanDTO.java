package com.miyu.cloud.mcs.dto.externalManufacture;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class McsMaterialPlanDTO {

    //物料条码
    @Schema(description = "物料唯一码")
    private String barCode;
    //设备编码
    @Schema(description = "设备编码")
    private String deviceNumber;
    //计划开始时间
    @Schema(description = "计划开始时间")
    private LocalDateTime planStartTime;
    //计划结束时间
    @Schema(description = "计划结束时间")
    private LocalDateTime planEndTime;
    //物料条码
    @Schema(description = "物料任务唯一码")
    private String recordNumber;
}
