package com.miyu.module.mcc.controller.admin.encodingclassification.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import java.util.*;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;
import com.alibaba.excel.annotation.*;

import javax.validation.constraints.NotNull;

@Schema(description = "管理后台 - 编码分类 Response VO")
@Data
@ExcelIgnoreUnannotated
public class EncodingClassificationRespVO {

    @Schema(description = "ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "7743")
    @ExcelProperty("ID")
    private String id;

    @Schema(description = "创建时间", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("创建时间")
    private LocalDateTime createTime;

    @Schema(description = "编码分类CODE", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("编码分类CODE")
    private String code;

    @Schema(description = "编码分类名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "李四")
    @ExcelProperty("编码分类名称")
    private String name;

    @Schema(description = "分类所属服务", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("分类所属服务")
    private String service;

    @Schema(description = "分类查看编码使用地址", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("分类查看编码使用地址")
    private String path;


}