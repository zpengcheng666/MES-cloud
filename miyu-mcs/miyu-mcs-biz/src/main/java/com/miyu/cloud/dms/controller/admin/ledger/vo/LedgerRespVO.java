package com.miyu.cloud.dms.controller.admin.ledger.vo;

import cn.iocoder.yudao.framework.excel.core.annotations.DictFormat;
import cn.iocoder.yudao.framework.excel.core.convert.DictConvert;
import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import com.miyu.cloud.mcs.dto.schedule.utils.IntervalVarList;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

@Schema(description = "管理后台 - 设备台账 Response VO")
@Data
@ExcelIgnoreUnannotated
public class LedgerRespVO {

    @Schema(description = "id", requiredMode = Schema.RequiredMode.REQUIRED, example = "26306")
    @ExcelProperty("id")
    private String id;

    @Schema(description = "设备/工位编号")
    @ExcelProperty("设备/工位编号")
    private String code;

    @Schema(description = "设备/工位名称", example = "张三")
    @ExcelProperty("设备/工位名称")
    private String name;

    @Schema(description = "所属产线/工位组")
    @ExcelProperty("所属产线/工位组")
    private String lintStationGroup;

    @Schema(description = "设备/工位类型")
    @ExcelProperty("设备/工位类型")
    private String equipmentStationType;

    @Schema(description = "设备/工位")
    @ExcelProperty(value = "设备/工位", converter = DictConvert.class)
    @DictFormat("dms_equipment_station")
    private Integer type;

    @Schema(description = "状态")
    @ExcelProperty(value = "状态", converter = DictConvert.class)
    @DictFormat("dms_device_status")
    private Integer status;

    @Schema(description = "运行状态")
    @ExcelProperty(value = "运行状态", converter = DictConvert.class)
    @DictFormat("dms_device_status_run")
    private Integer runStatus;

    @Schema(description = "在线状态")
    @ExcelProperty(value = "在线状态", converter = DictConvert.class)
    @DictFormat("dms_device_status_online")
    private Integer onlineStatus;

    @Schema(description = "是否需要配送料")
    @ExcelProperty(value = "是否需要配送料", converter = DictConvert.class)
    private Boolean needMaterials;

    @Schema(description = "位置")
    @ExcelProperty("位置")
    private String locationId;

    @Schema(description = "本机ip")
    @ExcelProperty("本机ip")
    private String ip;

    @Schema(description = "绑定设备id")
    @ExcelProperty("绑定设备id")
    private String bindEquipment;

    @Schema(description = "负责人")
    @ExcelProperty("负责人")
    private String superintendent;

    @Schema(description = "采购日期")
    @ExcelProperty("采购日期")
    private LocalDateTime purchaseDate;

    @Schema(description = "维护日期")
    @ExcelProperty("维护日期")
    private LocalDateTime maintenanceDate;

    @Schema(description = "维护人员")
    @ExcelProperty("维护人员")
    private String maintenanceBy;

    @Schema(description = "检查日期")
    @ExcelProperty("检查日期")
    private LocalDateTime inspectionDate;

    @Schema(description = "检查人员")
    @ExcelProperty("检查人员")
    private String inspectionBy;

    @Schema(description = "参数1")
    @ExcelProperty("参数1")
    private String technicalParameter1;

    @Schema(description = "参数2")
    @ExcelProperty("参数2")
    private String technicalParameter2;

    @Schema(description = "参数3")
    @ExcelProperty("参数3")
    private String technicalParameter3;

    @Schema(description = "参数4")
    @ExcelProperty("参数4")
    private String technicalParameter4;

    @Schema(description = "创建时间")
    @ExcelProperty("创建时间")
    private LocalDateTime createTime;

    /**
     * 用于存储设备所有可选的任务
     */
    private IntervalVarList machineIntervals = new IntervalVarList();
}
