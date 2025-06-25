package com.miyu.module.pdm.controller.admin.processRouteTypical.vo;

import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema(description = "管理后台 - 典型工艺路线 Request VO")
@Data
public class ProcessRouteTypicalListReqVO {
    @TableId
    @Schema(description = "id", example = "20041")
    private String id;
    //典型工艺路线名
    @Schema(description = "典型工艺路线名", example = "20041")
    private String name;
    //工序名称
    @Schema(description = "工序名称", example = "20041")
    private String procedureName;
    //工艺路线
    @Schema(description = "工艺路线", example = "20041")
    private String processRouteName;
    //典型工艺路线描述
    @Schema(description = "典型工艺路线描述", example = "20041")
    private String description;
}
