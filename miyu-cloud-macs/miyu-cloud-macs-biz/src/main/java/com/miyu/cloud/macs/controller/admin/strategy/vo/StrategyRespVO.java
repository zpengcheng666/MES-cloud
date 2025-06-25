package com.miyu.cloud.macs.controller.admin.strategy.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import java.util.*;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;
import com.alibaba.excel.annotation.*;

@Schema(description = "管理后台 - 策略 Response VO")
@Data
@ExcelIgnoreUnannotated
public class StrategyRespVO {

    @Schema(description = "主键id", requiredMode = Schema.RequiredMode.REQUIRED, example = "1658")
    @ExcelProperty("主键id")
    private String id;

    @Schema(description = "编码")
    @ExcelProperty("编码")
    private String code;

    @Schema(description = "名称", example = "李四")
    @ExcelProperty("名称")
    private String name;

    @Schema(description = "描述", example = "随便")
    @ExcelProperty("描述")
    private String description;

    @Schema(description = "状态（1启用 0不启用）", example = "1")
    @ExcelProperty("状态（1启用 0不启用）")
    private Integer status;

    @Schema(description = "创建人")
    @ExcelProperty("创建人")
    private String createBy;

    @Schema(description = "创建时间")
    @ExcelProperty("创建时间")
    private LocalDateTime createTime;

    @Schema(description = "更新人")
    @ExcelProperty("更新人")
    private String updateBy;

}