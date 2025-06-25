package com.miyu.cloud.dms.controller.admin.maintainapplication.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

@Schema(description = "管理后台 - 设备维修申请新增/修改 Request VO")
@Data
public class MaintainApplicationSaveReqVO {

    @Schema(description = "id", requiredMode = Schema.RequiredMode.REQUIRED, example = "6665")
    private String id;

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

}
