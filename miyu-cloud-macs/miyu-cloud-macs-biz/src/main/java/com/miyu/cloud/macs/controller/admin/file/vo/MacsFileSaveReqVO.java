package com.miyu.cloud.macs.controller.admin.file.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import javax.validation.constraints.*;

@Schema(description = "管理后台 - 文件新增/修改 Request VO")
@Data
public class MacsFileSaveReqVO {

    @Schema(description = "主键id", requiredMode = Schema.RequiredMode.REQUIRED, example = "22165")
    private String id;

    @Schema(description = "文件名", example = "王五")
    private String name;

    @Schema(description = "文件路径", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotEmpty(message = "文件路径不能为空")
    private String path;

    @Schema(description = "文件类型", example = "2")
    private String type;

    @Schema(description = "文件大小", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "文件大小不能为空")
    private Integer size;

    @Schema(description = "用户id", example = "31481")
    private String userId;

    @Schema(description = "访客id", example = "6672")
    private String visitorId;

}
