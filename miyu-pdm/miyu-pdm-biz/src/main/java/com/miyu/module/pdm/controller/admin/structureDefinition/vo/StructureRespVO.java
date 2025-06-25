package com.miyu.module.pdm.controller.admin.structureDefinition.vo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

@Schema(description = "管理后台 - PDM 数据包结构 Response VO")
@Data
@ExcelIgnoreUnannotated
public class StructureRespVO {

    @Schema(description = "结构编号", example = "1024")
    private Long id;

    @Schema(description = "父结构编号", example = "1024")
    private Long parentId;

    @Schema(description = "节点类型（0根节点 1结构 2文件夹 3文件）", requiredMode = Schema.RequiredMode.REQUIRED, example = "0")
    private Integer type;

    @Schema(description = "节点名称（结构/文件夹/文件）", requiredMode = Schema.RequiredMode.REQUIRED, example = "芋道")
    private String name;

    @Schema(description = "压缩包方式（zip）", requiredMode = Schema.RequiredMode.REQUIRED, example = "zip")
    private String compressType;

    @Schema(description = "设计单位", example = "test")
    private String designUnit;

    @Schema(description = "状态,见 CommonStatusEnum 枚举", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    private Integer status;

    @Schema(description = "文件类型（0CATPart 1pdf 2excel 3xml等）", example = "0")
    private Integer fileType;

    @Schema(description = "文件夹路径", example = "1")
    private String dirPath;

    @Schema(description = "绝对路径", example = "1")
    private String absolutePath;

    @Schema(description = "excel列表起始行", example = "1")
    private Integer startRow;

    @Schema(description = "创建时间", requiredMode = Schema.RequiredMode.REQUIRED, example = "时间戳格式")
    private LocalDateTime createTime;
}
