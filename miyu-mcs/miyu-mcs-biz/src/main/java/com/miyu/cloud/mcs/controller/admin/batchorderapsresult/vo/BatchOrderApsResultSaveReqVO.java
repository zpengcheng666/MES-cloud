package com.miyu.cloud.mcs.controller.admin.batchorderapsresult.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import javax.validation.constraints.*;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;

@Schema(description = "管理后台 - 排产结果新增/修改 Request VO")
@Data
public class BatchOrderApsResultSaveReqVO {

    @Schema(description = "id", requiredMode = Schema.RequiredMode.REQUIRED, example = "17445")
    private String id;

    @Schema(description = "排产开始时间")
    private LocalDateTime startTime;

    @Schema(description = "排产结果")
    private String apsContent;

    @Schema(description = "状态:1新创建,2已提交,3已通过,4作废", example = "1")
    private Integer status;

    @Schema(description = "提交时间")
    private LocalDateTime submitedTime;

    @Schema(description = "通过时间")
    private LocalDateTime acceptedTime;

    @Schema(description = "审批人")
    private String acceptedBy;

    @Schema(description = "作废时间")
    private LocalDateTime nullifiedTime;

    @Schema(description = "作废人")
    private String nullifiedBy;

    @Schema(description = "排产方式")
    private String planPriority;

    @Schema(description = "排产结果")
    private String apsConfig;

    @Schema(description = "所属部门(这个没用吧)")
    private String sysOrgCode;

}
