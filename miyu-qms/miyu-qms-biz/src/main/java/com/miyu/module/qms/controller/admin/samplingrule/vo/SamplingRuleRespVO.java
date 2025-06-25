package com.miyu.module.qms.controller.admin.samplingrule.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import java.util.*;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;
import com.alibaba.excel.annotation.*;
import cn.iocoder.yudao.framework.excel.core.annotations.DictFormat;
import cn.iocoder.yudao.framework.excel.core.convert.DictConvert;

@Schema(description = "管理后台 - 抽样规则 Response VO")
@Data
@ExcelIgnoreUnannotated
public class SamplingRuleRespVO {

    @Schema(description = "主键", requiredMode = Schema.RequiredMode.REQUIRED, example = "4339")
    @ExcelProperty("主键")
    private String id;

    @Schema(description = "创建时间", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("创建时间")
    private LocalDateTime createTime;

    @Schema(description = "抽样标准ID", example = "32034")
    @ExcelProperty("抽样标准ID")
    private String samplingStandardId;

    @Schema(description = "样本数字码", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty(value = "样本数字码", converter = DictConvert.class)
    @DictFormat("sample_size_code") // TODO 代码优化：建议设置到对应的 DictTypeConstants 枚举类中
    private String sampleSizeCode;

    @Schema(description = "批量下限值（N）")
    @ExcelProperty("批量下限值（N）")
    private Integer minValue;

    @Schema(description = "批量上限值（N）")
    @ExcelProperty("批量上限值（N）")
    private Integer maxValue;

    @Schema(description = "检验水平类型", example = "2")
    @ExcelProperty(value = "检验水平类型", converter = DictConvert.class)
    @DictFormat("inspection_level_type") // TODO 代码优化：建议设置到对应的 DictTypeConstants 枚举类中
    private Integer inspectionLevelType;

}