package com.miyu.module.mcc.controller.admin.encodingattribute.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import java.util.*;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;
import com.alibaba.excel.annotation.*;

@Schema(description = "管理后台 - 编码自定义属性 Response VO")
@Data
@ExcelIgnoreUnannotated
public class EncodingAttributeRespVO {

    @Schema(description = "ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "32519")
    @ExcelProperty("ID")
    private String id;
    @Schema(description = "创建时间", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("创建时间")
    private LocalDateTime createTime;

    @Schema(description = "属性名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "芋艿")
    @ExcelProperty("属性名称")
    private String name;

    @Schema(description = "属性值", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("属性值")
    private String code;

}