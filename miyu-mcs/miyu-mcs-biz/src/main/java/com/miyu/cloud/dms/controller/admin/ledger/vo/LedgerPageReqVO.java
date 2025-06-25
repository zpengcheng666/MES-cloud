package com.miyu.cloud.dms.controller.admin.ledger.vo;

import cn.iocoder.yudao.framework.common.pojo.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

import static cn.iocoder.yudao.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

@Schema(description = "管理后台 - 设备台账分页 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class LedgerPageReqVO extends PageParam {

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
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] purchaseDate;

    @Schema(description = "维护日期")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] maintenanceDate;

    @Schema(description = "维护人员")
    private String maintenanceBy;

    @Schema(description = "检查日期")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] inspectionDate;

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

    @Schema(description = "创建时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] createTime;

}
