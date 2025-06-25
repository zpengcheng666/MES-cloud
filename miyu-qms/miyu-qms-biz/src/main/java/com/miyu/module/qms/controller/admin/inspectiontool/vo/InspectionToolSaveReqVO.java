package com.miyu.module.qms.controller.admin.inspectiontool.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDateTime;
import java.util.*;
import javax.validation.constraints.*;

@Schema(description = "管理后台 - 检测工具新增/修改 Request VO")
@Data
public class InspectionToolSaveReqVO {

    @Schema(description = "主键", requiredMode = Schema.RequiredMode.REQUIRED, example = "21822")
    private String id;

    @Schema(description = "检测工具名称", example = "张三")
    private String name;

    @Schema(description = "规格", example = "21822")
    private String spec;

    @Schema(description = "测量范围", example = "张三")
    private String measuringRange;

    @Schema(description = "准确等级", example = "21822")
    private String accuracyLevel;

    @Schema(description = "制造商", example = "张三")
    private String manufacturer;

    @Schema(description = "出厂编号", example = "21822")
    private String manufacturerNumber;

    @Schema(description = "本厂编号",requiredMode = Schema.RequiredMode.REQUIRED, example = "21822")
    @NotEmpty(message = "本厂编号不能为空")
    private String barCode;

    @Schema(description = "状态",requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @NotEmpty(message = "状态不能为空")
    private String status;

    @Schema(description = "检/校准日期", example = "21822")
    private LocalDateTime verificationDate;

    @Schema(description = "检/校定周期", example = "张三")
    private Integer verificationCycle;

    @Schema(description = "库存主键", requiredMode = Schema.RequiredMode.REQUIRED, example = "21822")
    @NotEmpty(message = "库存ID不能为空")
    private String stockId;

    @Schema(description = "物料类型主键", requiredMode = Schema.RequiredMode.REQUIRED, example = "21822")
    @NotEmpty(message = "物料类型不能为空")
    private String materialConfigId;
}