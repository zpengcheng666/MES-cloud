package com.miyu.cloud.macs.controller.admin.file.vo;

import lombok.*;
import io.swagger.v3.oas.annotations.media.Schema;
import cn.iocoder.yudao.framework.common.pojo.PageParam;

@Schema(description = "管理后台 - 文件分页 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class MacsFilePageReqVO extends PageParam {

    @Schema(description = "文件名", example = "王五")
    private String name;

    @Schema(description = "文件路径")
    private String path;

    @Schema(description = "文件类型", example = "2")
    private String type;

    @Schema(description = "文件大小")
    private Integer size;

    @Schema(description = "用户id", example = "31481")
    private String userId;

    @Schema(description = "访客id", example = "6672")
    private String visitorId;

}
