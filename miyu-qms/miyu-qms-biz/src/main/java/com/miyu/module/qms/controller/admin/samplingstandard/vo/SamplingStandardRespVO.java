package com.miyu.module.qms.controller.admin.samplingstandard.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import java.util.*;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;
import com.alibaba.excel.annotation.*;

@Schema(description = "管理后台 - 抽样标准 Response VO")
@Data
@ExcelIgnoreUnannotated
public class SamplingStandardRespVO {

    @Schema(description = "主键", requiredMode = Schema.RequiredMode.REQUIRED, example = "22304")
    @ExcelProperty("主键")
    private String id;

    @Schema(description = "创建时间", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("创建时间")
    private LocalDateTime createTime;

    @Schema(description = "父抽样标准ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "645")
    @ExcelProperty("父抽样标准ID")
    private String parentId;

    @Schema(description = "抽样标准名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "王五")
    @ExcelProperty("抽样标准名称")
    private String name;

}