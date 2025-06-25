package com.miyu.module.qms.controller.admin.samplingstandard.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import javax.validation.constraints.*;

@Schema(description = "管理后台 - 抽样标准新增/修改 Request VO")
@Data
public class SamplingStandardSaveReqVO {

    @Schema(description = "主键", requiredMode = Schema.RequiredMode.REQUIRED, example = "22304")
    private String id;

    @Schema(description = "父抽样标准ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "645")
    @NotEmpty(message = "父抽样标准ID不能为空")
    private String parentId;

    @Schema(description = "抽样标准名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "王五")
    @NotEmpty(message = "抽样标准名称不能为空")
    private String name;

}