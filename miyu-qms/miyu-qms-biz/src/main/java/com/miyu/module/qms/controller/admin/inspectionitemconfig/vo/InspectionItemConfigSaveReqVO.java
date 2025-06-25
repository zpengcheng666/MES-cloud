package com.miyu.module.qms.controller.admin.inspectionitemconfig.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import javax.validation.constraints.*;

@Schema(description = "管理后台 - 检测项配置表（检测内容名称）新增/修改 Request VO")
@Data
public class InspectionItemConfigSaveReqVO {

    @Schema(description = "主键", requiredMode = Schema.RequiredMode.REQUIRED, example = "19235")
    private String id;

    @Schema(description = "检测项配置表名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "李四")
    @NotEmpty(message = "检测项配置表名称不能为空")
    private String name;

    @Schema(description = "编号")
    private String no;

}