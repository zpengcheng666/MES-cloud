package com.miyu.cloud.macs.controller.admin.userRoleRegion.vo;

import lombok.*;
import java.util.*;
import io.swagger.v3.oas.annotations.media.Schema;
import cn.iocoder.yudao.framework.common.pojo.PageParam;

@Schema(description = "管理后台 - 用户角色分页 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class UserRoleRegionPageReqVO extends PageParam {

    @Schema(description = "区域id", example = "25970")
    private String regionId;

    @Schema(description = "用户id", example = "15369")
    private String userId;

    @Schema(description = "角色id", example = "25785")
    private String roleId;

}