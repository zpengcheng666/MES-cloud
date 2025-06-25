package com.miyu.cloud.macs.controller.admin.region.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import javax.validation.constraints.*;

@Schema(description = "管理后台 - 区域新增/修改 Request VO")
@Data
public class RegionSaveReqVO {

    @Schema(description = "主键id", requiredMode = Schema.RequiredMode.REQUIRED, example = "3301")
    private String id;

    @Schema(description = "区域编码")
    private String code;

    @Schema(description = "区域名称", example = "李四")
    private String name;

    @Schema(description = "公开状态(0未公开,1公开)", example = "2")
    private Boolean publicStatus;

    @Schema(description = "描述", example = "你说的对")
    private String description;

    @Schema(description = "上级id", example = "20787")
    private String parentId;

    @Schema(description = "创建人")
    private String createBy;

    @Schema(description = "更新人")
    private String updateBy;

}