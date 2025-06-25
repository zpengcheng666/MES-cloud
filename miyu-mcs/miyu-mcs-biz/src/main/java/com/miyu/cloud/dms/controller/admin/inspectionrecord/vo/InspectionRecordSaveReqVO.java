package com.miyu.cloud.dms.controller.admin.inspectionrecord.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

@Schema(description = "管理后台 - 设备检查记录新增/修改 Request VO")
@Data
public class InspectionRecordSaveReqVO {

    @Schema(description = "id", requiredMode = Schema.RequiredMode.REQUIRED, example = "18122")
    private String id;

    @Schema(description = "计划编码")
    private String code;

    @Schema(description = "记录状态")
    private Integer status;

    @Schema(description = "设备")
    private String device;

    @Schema(description = "是否超期停机", example = "2")
    private Integer expirationShutdown;

    @Schema(description = "超期时间")
    private Integer expirationTime;

    @Schema(description = "检查类型", example = "1")
    private Integer type;

    @Schema(description = "备注", example = "随便")
    private String remark;

    @Schema(description = "检查内容")
    private String content;

    @Schema(description = "检查人")
    private String createBy;

    @Schema(description = "开始时间")
    private LocalDateTime startTime;

    @Schema(description = "结束时间")
    private LocalDateTime endTime;

}
