package com.miyu.module.tms.controller.admin.toolrecord.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import javax.validation.constraints.*;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;

@Schema(description = "管理后台 - 刀具使用记录新增/修改 Request VO")
@Data
public class ToolRecordSaveReqVO {

    @Schema(description = "ID", requiredMode = Schema.RequiredMode.REQUIRED)
    private String id;

    @Schema(description = "成品刀具id")
    private String toolInfoId;

    @Schema(description = "起始时间")
    private LocalDateTime startTime;

    @Schema(description = "结束时间")
    private LocalDateTime endTime;

    @Schema(description = "表字段id（目标设备、目标位置、等）")
    private String field;

    @Schema(description = "类型（装配、测量、出库、配送、上机、使用、下机、回库、入库、拆卸）")
    private Integer type;

}