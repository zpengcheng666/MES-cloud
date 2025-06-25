package com.miyu.module.qms.controller.admin.inspectiontoolverificationrecord.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import javax.validation.constraints.*;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;

@Schema(description = "管理后台 - 检验工具校准记录新增/修改 Request VO")
@Data
public class InspectionToolVerificationRecordSaveReqVO {

    @Schema(description = "主键", requiredMode = Schema.RequiredMode.REQUIRED, example = "5001")
    private String id;

    @Schema(description = "库存主键", example = "4249")
    private String stockId;

    @Schema(description = "工具主键", example = "13328")
    private String toolId;

    @Schema(description = "送检时间")
    private LocalDateTime verificationDateBegin;

    @Schema(description = "实际送检时间")
    private LocalDateTime verificationDateBeginAct;

    @Schema(description = "完成时间")
    private LocalDateTime verificationDateEnd;

    @Schema(description = "送检结果", example = "13328")
    private String remark;

    @Schema(description = "状态", example = "1")
    private Integer status;

}
