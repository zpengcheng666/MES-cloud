package com.miyu.module.pdm.controller.admin.project.vo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

@Schema(description = "管理后台 - PDM 项目 Response VO")
@Data
@ExcelIgnoreUnannotated
public class ProjectRespVO {

    @Schema(description = "id", requiredMode = Schema.RequiredMode.REQUIRED, example = "5860")
    @ExcelProperty("id")
    private String id;

    @Schema(description = "项目名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "21829")
    @ExcelProperty("项目名称")
    private String name;

    @Schema(description = "项目编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "芋艿")
    @ExcelProperty("项目编号")
    private String code;

    @Schema(description = "创建时间", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("创建时间")
    private LocalDateTime createTime;

}
