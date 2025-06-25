package com.miyu.cloud.mcs.controller.admin.batchrecordstep.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import javax.validation.constraints.*;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;

@Schema(description = "管理后台 - 工步计划新增/修改 Request VO")
@Data
public class BatchRecordStepSaveReqVO {

    @Schema(description = "id", requiredMode = Schema.RequiredMode.REQUIRED, example = "22173")
    private String id;

    @Schema(description = "工序任务id", example = "21877")
    private String batchRecordId;

    @Schema(description = "工步id", example = "16737")
    private String stepId;

    @Schema(description = "工步名称", example = "王五")
    private String stepName;

    @Schema(description = "工步顺序号")
    private String stepOrder;

    @Schema(description = "设备类型", example = "17848")
    private String deviceTypeId;

    @Schema(description = "指定设备id集合")
    private String defineDeviceIds;

    @Schema(description = "计划开始时间")
    private LocalDateTime planStartTime;

    @Schema(description = "计划结束时间")
    private LocalDateTime planEndTime;

}
