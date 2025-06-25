package com.miyu.cloud.dms.controller.admin.ledger.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

@Schema(description = "管理后台 - 设备台账新增/修改 Request VO")
@Data
public class LedgerSaveReqVO {

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

    @Schema(description = "是否需要配送料")
    private Boolean needMaterials;

    @Schema(description = "位置")
    private String locationId;

    @Schema(description = "本机ip")
    private String ip;

    @Schema(description = "绑定设备id")
    private String bindEquipment;

    @Schema(description = "负责人")
    private String superintendent;

    @Schema(description = "采购日期")
    private LocalDateTime purchaseDate;

    @Schema(description = "维护日期")
    private LocalDateTime maintenanceDate;

    @Schema(description = "维护人员")
    private String maintenanceBy;

    @Schema(description = "检查日期")
    private LocalDateTime inspectionDate;

    @Schema(description = "检查人员")
    private String inspectionBy;

    @Schema(description = "参数1")
    private String technicalParameter1;

    @Schema(description = "参数2")
    private String technicalParameter2;

    @Schema(description = "参数3")
    private String technicalParameter3;

    @Schema(description = "参数4")
    private String technicalParameter4;
}
