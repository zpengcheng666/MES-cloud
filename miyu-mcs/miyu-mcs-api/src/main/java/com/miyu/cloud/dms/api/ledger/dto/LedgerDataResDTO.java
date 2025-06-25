package com.miyu.cloud.dms.api.ledger.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Schema(description = "设备台账 Response DTO")
@Data
public class LedgerDataResDTO {

    @Schema(description = "id", requiredMode = Schema.RequiredMode.REQUIRED, example = "26306")
    private String id;

    @Schema(description = "设备/工位编号")
    private String code;

    @Schema(description = "设备/工位名称", example = "张三")
    private String name;

    @Schema(description = "所属产线/工位组")
    private String lintStationGroup;

    @Schema(description = "设备/工位类型")
    private String equipmentStationType;

    @Schema(description = "设备/工位")
    private Integer type;

    @Schema(description = "状态")
    private Integer status;

    @Schema(description = "运行状态")
    private Integer runStatus;

    @Schema(description = "在线状态")
    private Integer onlineStatus;

    @Schema(description = "位置")
    private String locationId;

    @Schema(description = "设备操作人")
    private List<String> users;

    @Schema(description = "设备负责人")
    private String superintendent;

}
