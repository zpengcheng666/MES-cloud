package com.miyu.module.wms.controller.admin.unit.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import javax.validation.constraints.*;

@Schema(description = "管理后台 - 单位新增/修改 Request VO")
@Data
public class UnitSaveReqVO {

    @Schema(description = "ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "31521")
    private String id;

    @Schema(description = "单位", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotEmpty(message = "单位不能为空")
    private String unit;

}