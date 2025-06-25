package com.miyu.cloud.macs.controller.admin.strategy.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import javax.validation.constraints.*;

@Schema(description = "管理后台 - 策略新增/修改 Request VO")
@Data
public class StrategySaveReqVO {

    @Schema(description = "主键id", requiredMode = Schema.RequiredMode.REQUIRED, example = "1658")
    private String id;

    @Schema(description = "编码")
    private String code;

    @Schema(description = "名称", example = "李四")
    private String name;

    @Schema(description = "描述", example = "随便")
    private String description;

    @Schema(description = "状态（1启用 0不启用）", example = "1")
    private Integer status;

    @Schema(description = "创建人")
    private String createBy;

    @Schema(description = "更新人")
    private String updateBy;

}