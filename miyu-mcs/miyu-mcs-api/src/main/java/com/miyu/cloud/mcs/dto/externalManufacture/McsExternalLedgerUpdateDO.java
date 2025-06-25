package com.miyu.cloud.mcs.dto.externalManufacture;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class McsExternalLedgerUpdateDO {

    //id
    @Schema(description = "设备id(id与编码必填一个)", example = "123456")
    private String id;
    //设备/工位编号
    @Schema(description = "设备编码(id与编码必填一个)", example = "abc")
    private String code;
    //设备/工位名称
    @Schema(description = "设备名称", example = "xx设备")
    private String name;
    //设备/工位类型编码
    @Schema(description = "设备类型编码(新增必填)", example = "tabc")
    private String deviceTypeCode;
    //产线编码
    @Schema(description = "产线编码(新增必填)", example = "ABC")
    private String lineCode;
    //设备/工位
    @Schema(description = "设备类别(默认0:设备)", example = "0")
    private Integer type = 0;
    //状态
    @Schema(description = "设备状态(0:正常;1:禁用;2:维修)", example = "1")
    private Integer status;
    //运行状态
    @Schema(description = "设备状态(0:加工中;1:空闲;2:停机;3:暂停;4:急停;5:报警)", example = "1")
    private Integer runStatus;
    //在线状态
    @Schema(description = "设备状态(0:在线;1:半离线;2:离线)", example = "1")
    private Integer onlineStatus;
    //是否可配送料
    @Schema(description = "是否可配送物料", example = "false")
    private Boolean deliverable;
    //负责人
    @Schema(description = "负责人", example = "123456")
    private String superintendent;
    //采购日期
    @Schema(description = "采购日期")
    private LocalDateTime purchaseDate;
    //最新维护日期
    @Schema(description = "最新维护日期")
    private LocalDateTime maintenanceDate;
    //最新维护人员
    @Schema(description = "最新维护人员")
    private String maintenanceBy;
    //最新检查日期
    @Schema(description = "最新检查日期")
    private LocalDateTime inspectionDate;
    //最新检查人员
    @Schema(description = "最新检查人员")
    private String inspectionBy;

}
