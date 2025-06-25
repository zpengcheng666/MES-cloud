package com.miyu.module.tms.controller.admin.fitconfig.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import javax.validation.constraints.*;

@Schema(description = "管理后台 - 刀具适配新增/修改 Request VO")
@Data
public class FitConfigSaveReqVO {

    @Schema(description = "ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "14740")
    private String id;

    @Schema(description = "刀具类型id", example = "22693")
    private String toolConfigId;

    @Schema(description = "物料类型id", example = "21882")
    private String materialConfigId;

    @Schema(description = "物料类型id")
    private String materialConfigId2;

}