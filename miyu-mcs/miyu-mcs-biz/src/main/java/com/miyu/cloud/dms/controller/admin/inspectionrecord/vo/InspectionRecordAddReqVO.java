package com.miyu.cloud.dms.controller.admin.inspectionrecord.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

@Schema(description = "管理后台 - 设备检查记录添加 Request VO")
@Data
public class InspectionRecordAddReqVO {
    @Schema(description = "ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "18122")
    private String id;

    @Schema(description = "备注", example = "随便")
    private String remark;

    @Schema(description = "检查内容")
    private String content;

    @Schema(description = "开始时间")
    private LocalDateTime startTime;

    @Schema(description = "结束时间")
    private LocalDateTime endTime;
}
