package com.miyu.module.pdm.controller.admin.processRoute.vo;

import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema(description = "管理后台 - 工艺方案 Request VO")
@Data
public class ProcessRouteListReqVO {
    @TableId
    @Schema(description = "id", example = "20041")
    private String id;
    //加工路线名
    @Schema(description = "加工路线名", example = "20041")
    private String name;
    //加工路线描述
    @Schema(description = "加工路线描述", example = "20041")
    private String description;
}
