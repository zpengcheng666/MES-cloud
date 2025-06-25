package com.miyu.cloud.mcs.dto.resource;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class McsDeviceDTO {

    private String id;
    @Schema(description = "设备/工位编号")
    private String code;
    /**
     * 设备/工位名称
     */
    @Schema(description = "设备/工位名称")
    private String name;
    /**
     * 所属产线/工位组
     */
    @Schema(description = "所属产线/工位组")
    private String lintStationGroup;
    /**
     * 设备/工位类型
     */
    @Schema(description = "设备/工位类型")
    private String equipmentStationType;
    /**
     * 设备/工位
     */
    @Schema(description = "设备/工位")
    private Integer type;
    /**
     * 状态
     */
    @Schema(description = "状态")
    private Integer status;
    /**
     * 状态
     */
    @Schema(description = "状态")
    private Integer runStatus;
    /**
     * 在线状态
     */
    @Schema(description = "在线状态")
    private Integer onlineStatus;
    /**
     * 是否需要配送料
     */
    @Schema(description = "是否需要配送料")
    private Boolean needMaterials;
    /**
     * 位置
     */
    @Schema(description = "位置")
    private String locationId;
    /**
     * 本机ip
     */
    @Schema(description = "本机ip")
    private String ip;
    /**
     * 绑定设备id
     */
    @Schema(description = "绑定设备id")
    private String bindEquipment;
    /**
     * 负责人
     */
    @Schema(description = "负责人")
    private String superintendent;
    /**
     * 采购日期
     */
    @Schema(description = "采购日期")
    private LocalDateTime purchaseDate;
    /**
     * 维护日期
     */
    @Schema(description = "维护日期")
    private LocalDateTime maintenanceDate;
    /**
     * 维护人员
     */
    @Schema(description = "维护人员")
    private String maintenanceBy;
    /**
     * 检查日期
     */
    @Schema(description = "检查日期")
    private LocalDateTime inspectionDate;
    /**
     * 检查人员
     */
    @Schema(description = "检查人员")
    private String inspectionBy;
    /**
     * 参数1
     */
    @Schema(description = "参数1")
    private String technicalParameter1;
    /**
     * 参数2
     */
    @Schema(description = "设备/工位编号")
    private String technicalParameter2;
    /**
     * 参数3
     */
    @Schema(description = "设备/工位编号")
    private String technicalParameter3;
    /**
     * 参数4
     */
    @Schema(description = "设备/工位编号")
    private String technicalParameter4;
}
