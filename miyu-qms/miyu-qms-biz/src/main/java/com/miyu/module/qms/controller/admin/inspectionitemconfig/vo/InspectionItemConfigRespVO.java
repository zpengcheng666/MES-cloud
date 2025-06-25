package com.miyu.module.qms.controller.admin.inspectionitemconfig.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import java.util.*;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;
import com.alibaba.excel.annotation.*;

@Schema(description = "管理后台 - 检测项配置表（检测内容名称） Response VO")
@Data
@ExcelIgnoreUnannotated
public class InspectionItemConfigRespVO {

    @Schema(description = "主键", requiredMode = Schema.RequiredMode.REQUIRED, example = "19235")
    @ExcelProperty("主键")
    private String id;

    @Schema(description = "创建时间", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("创建时间")
    private LocalDateTime createTime;

    @Schema(description = "检测项配置表名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "李四")
    @ExcelProperty("检测项配置表名称")
    private String name;

    @Schema(description = "编号")
    @ExcelProperty("编号")
    private String no;

}