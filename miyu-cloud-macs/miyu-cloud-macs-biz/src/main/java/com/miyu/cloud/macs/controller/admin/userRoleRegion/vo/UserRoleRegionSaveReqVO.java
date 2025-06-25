package com.miyu.cloud.macs.controller.admin.userRoleRegion.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import javax.validation.constraints.*;

@Schema(description = "管理后台 - 用户角色新增/修改 Request VO")
@Data
public class UserRoleRegionSaveReqVO {

    @Schema(description = "主键id", requiredMode = Schema.RequiredMode.REQUIRED, example = "1195")
    private String id;

    @Schema(description = "区域id", example = "25970")
    private String regionId;

    @Schema(description = "用户id", example = "15369")
    private String userId;

    @Schema(description = "角色id", example = "25785")
    private String roleId;

}