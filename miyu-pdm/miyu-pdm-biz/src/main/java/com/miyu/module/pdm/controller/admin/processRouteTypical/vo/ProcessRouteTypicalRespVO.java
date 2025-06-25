package com.miyu.module.pdm.controller.admin.processRouteTypical.vo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Schema(description = "pdm - 典型工艺路线信息 Response VO")
@Data
@ExcelIgnoreUnannotated
public class ProcessRouteTypicalRespVO {
    @Schema(description = "典型工艺路线ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @ExcelProperty("典型工艺路线ID")
    private String id;

    @Schema(description = "典型工艺路线名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @NotBlank(message = "典型工艺路线名称不能为空")
    @ExcelProperty("典型工艺路线名称")
    private String name;

    @Schema(description = "工序名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @NotBlank(message = "工序名称不能为空")
    @ExcelProperty("工序名称")
    private String procedureName;

    @Schema(description = "工艺路线", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @NotBlank(message = "工艺路线不能为空")
    @ExcelProperty("工艺路线")
    private String processRouteName;

    @Schema(description = "典型工艺路线描述", requiredMode = Schema.RequiredMode.REQUIRED, example = "这是描述")
    @NotBlank(message = "典型工艺路线描述不能为空")
    @ExcelProperty("典型工艺路线描述")
    private String description;

    @Schema(description = "创建时间", requiredMode = Schema.RequiredMode.REQUIRED, example = "时间戳格式")
    @ExcelProperty("创建时间")
    private LocalDateTime createTime;
}
