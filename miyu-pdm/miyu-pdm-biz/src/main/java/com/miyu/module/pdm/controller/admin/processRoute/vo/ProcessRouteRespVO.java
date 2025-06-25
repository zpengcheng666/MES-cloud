package com.miyu.module.pdm.controller.admin.processRoute.vo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Schema(description = "pdm - 加工路线信息 Response VO")
@Data
@ExcelIgnoreUnannotated
public class ProcessRouteRespVO {
    @Schema(description = "加工路线ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @ExcelProperty("加工路线ID")
    private String id;

    @Schema(description = "加工路线名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "C系列")
    @NotBlank(message = "加工路线名称不能为空")
    @ExcelProperty("加工路线名称")
    private String name;

    @Schema(description = "加工路线描述", requiredMode = Schema.RequiredMode.REQUIRED, example = "这是描述")
    @NotBlank(message = "加工路线描述不能为空")
    @ExcelProperty("加工路线描述")
    private String description;

    @Schema(description = "创建时间", requiredMode = Schema.RequiredMode.REQUIRED, example = "时间戳格式")
    @ExcelProperty("创建时间")
    private LocalDateTime createTime;
}
