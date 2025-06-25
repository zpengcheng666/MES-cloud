package com.miyu.module.wms.controller.admin.checkplan.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import javax.validation.constraints.*;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;

@Schema(description = "管理后台 - 库存盘点计划新增/修改 Request VO")
@Data
public class CheckPlanSaveReqVO {

    @Schema(description = "ID", requiredMode = Schema.RequiredMode.REQUIRED)
    private String id;

    @Schema(description = "盘点库区id", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotEmpty(message = "盘点库区id不能为空")
    private String checkAreaId;

    @Schema(description = "盘点名称", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotEmpty(message = "盘点名称不能为空")
    private String checkName;

    @Schema(description = "物料类型ids")
    private List<String> materialConfigIds;

    @Schema(description = "开始时间")
    private LocalDateTime startTime;

    @Schema(description = "结束时间")
    private LocalDateTime endTime;

    @Schema(description = "截止时间", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "截止时间不能为空")
    private LocalDateTime cutOffTime;

    @Schema(description = "负责人")
    private String checkUserId;

    @Schema(description = "盘点状态", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "盘点状态不能为空")
    private Integer checkStatus;

    @Schema(description = "是否锁盘", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "是否锁盘不能为空")
    private Boolean checkLocked;

}