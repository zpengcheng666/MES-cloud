package com.miyu.module.qms.controller.admin.defectivecode.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.time.LocalDateTime;
import com.alibaba.excel.annotation.*;

@Schema(description = "管理后台 - 缺陷代码 Response VO")
@Data
@ExcelIgnoreUnannotated
public class DefectiveCodeRespVO {

    @Schema(description = "主键", requiredMode = Schema.RequiredMode.REQUIRED, example = "14495")
    private String id;

    @Schema(description = "缺陷代码名称", example = "王五")
    @ExcelProperty("缺陷代码名称")
    private String name;

    @Schema(description = "缺陷代码")
    @ExcelProperty("缺陷代码")
    private String code;

    @Schema(description = "创建时间", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("创建时间")
    private LocalDateTime createTime;

}