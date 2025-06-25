package com.miyu.cloud.macs.controller.admin.userRoleRegion.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import java.util.*;
import com.alibaba.excel.annotation.*;

@Schema(description = "管理后台 - 用户角色 Response VO")
@Data
@ExcelIgnoreUnannotated
public class UserRoleRegionRespVO {

    @Schema(description = "主键id", requiredMode = Schema.RequiredMode.REQUIRED, example = "1195")
    @ExcelProperty("主键id")
    private String id;

    @Schema(description = "区域id", example = "25970")
    @ExcelProperty("区域id")
    private String regionId;

    @Schema(description = "用户id", example = "15369")
    @ExcelProperty("用户id")
    private String userId;

    @Schema(description = "角色id", example = "25785")
    @ExcelProperty("角色id")
    private String roleId;

}