package com.miyu.cloud.macs.controller.admin.file.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import com.alibaba.excel.annotation.*;

@Schema(description = "管理后台 - 文件 Response VO")
@Data
@ExcelIgnoreUnannotated
public class MacsFileRespVO {

    @Schema(description = "主键id", requiredMode = Schema.RequiredMode.REQUIRED, example = "22165")
    @ExcelProperty("主键id")
    private String id;

    @Schema(description = "文件名", example = "王五")
    @ExcelProperty("文件名")
    private String name;

    @Schema(description = "文件路径", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("文件路径")
    private String path;

    @Schema(description = "文件类型", example = "2")
    @ExcelProperty("文件类型")
    private String type;

    @Schema(description = "文件大小", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("文件大小")
    private Integer size;

    @Schema(description = "用户id", example = "31481")
    @ExcelProperty("用户id")
    private String userId;

    @Schema(description = "访客id", example = "6672")
    @ExcelProperty("访客id")
    private String visitorId;

}
