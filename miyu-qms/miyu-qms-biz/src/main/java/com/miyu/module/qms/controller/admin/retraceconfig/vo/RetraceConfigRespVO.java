package com.miyu.module.qms.controller.admin.retraceconfig.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import java.util.*;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;
import com.alibaba.excel.annotation.*;

@Schema(description = "管理后台 - 追溯字段配置 Response VO")
@Data
@ExcelIgnoreUnannotated
public class RetraceConfigRespVO {

    @Schema(description = "主键", requiredMode = Schema.RequiredMode.REQUIRED, example = "27061")
    @ExcelProperty("主键")
    private String id;

    @Schema(description = "创建时间", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("创建时间")
    private LocalDateTime createTime;

    @Schema(description = "字段名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "张三")
    @ExcelProperty("字段名称")
    private String name;

    @Schema(description = "字段属性")
    @ExcelProperty("字段属性")
    private String no;

}