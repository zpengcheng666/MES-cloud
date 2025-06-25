package com.miyu.module.qms.controller.admin.inspectionitemtype.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import javax.validation.constraints.*;

@Schema(description = "管理后台 - 检测项目分类新增/修改 Request VO")
@Data
public class InspectionItemTypeSaveReqVO {

    @Schema(description = "主键", requiredMode = Schema.RequiredMode.REQUIRED, example = "12930")
    private String id;

    @Schema(description = "父项目分类ID", example = "6128")
    private String parentId;

    @Schema(description = "检测项目分类名称", example = "李四")
    private String name;

}