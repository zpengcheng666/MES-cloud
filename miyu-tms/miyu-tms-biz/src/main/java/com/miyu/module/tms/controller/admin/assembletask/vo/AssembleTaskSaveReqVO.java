package com.miyu.module.tms.controller.admin.assembletask.vo;

import com.miyu.module.tms.dal.dataobject.toolinfo.AssembleRecordDO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.time.LocalDateTime;
import java.util.List;

@Schema(description = "管理后台 - 刀具装配任务新增/修改 Request VO")
@Data
public class AssembleTaskSaveReqVO {

    @Schema(description = "ID", requiredMode = Schema.RequiredMode.REQUIRED)
    private String id;

    @Schema(description = "接单人")
    private String operator;

    @Schema(description = "工单号")
    private String orderNumber;

    @Schema(description = "需求数量")
    private Integer needCount;

    @Schema(description = "目标位置")
    private String targetLocation;

    @Schema(description = "配送截止时间")
    private LocalDateTime distributionDeadline;

    @Schema(description = "物料类型id")
    private String materialConfigId;

    @Schema(description = "最短加工时长")
    private Integer minimumTime;

    @Schema(description = "状态（启用、作废）")
    private Integer status;

    @Schema(description = "刀具装配记录列表")
    private List<AssembleRecordDO> assembleRecords;

}