package com.miyu.module.qms.controller.admin.samplingruleconfig.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import java.util.*;
import java.math.BigDecimal;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;
import com.alibaba.excel.annotation.*;
import cn.iocoder.yudao.framework.excel.core.annotations.DictFormat;
import cn.iocoder.yudao.framework.excel.core.convert.DictConvert;

@Schema(description = "管理后台 - 抽样规则（检验抽样方案） Response VO")
@Data
@ExcelIgnoreUnannotated
public class SamplingRuleConfigRespVO {

    @Schema(description = "主键", requiredMode = Schema.RequiredMode.REQUIRED, example = "21112")
    @ExcelProperty("主键")
    private String id;

    @Schema(description = "创建时间", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("创建时间")
    private LocalDateTime createTime;

    @Schema(description = "样本量字码", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty(value = "样本量字码", converter = DictConvert.class)
    @DictFormat("sample_size_code") // TODO 代码优化：建议设置到对应的 DictTypeConstants 枚举类中
    private String sampleSizeCode;

    @Schema(description = "抽取样本数量", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("抽取样本数量")
    private Integer sampleSize;

    @Schema(description = "类型  1正常检查2加严检查3放宽检查", requiredMode = Schema.RequiredMode.REQUIRED, example = "2")
    @ExcelProperty(value = "类型  1正常检查2加严检查3放宽检查", converter = DictConvert.class)
    @DictFormat("sampling_rule_type") // TODO 代码优化：建议设置到对应的 DictTypeConstants 枚举类中
    private Integer samplingRuleType;

    @Schema(description = "接收质量限（AQL）", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("接收质量限（AQL）")
    private BigDecimal acceptanceQualityLimit;

    @Schema(description = "接收数（Ac）", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("接收数（Ac）")
    private Integer acceptNum;

    @Schema(description = "拒收数（Re）", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("拒收数（Re）")
    private Integer rejectionNum;
    @Schema(description = "抽样标准ID", example = "23748")
    private String samplingStandardId;

    @Schema(description = "抽样标准名称", example = "23748")
    private String samplingStandardName;
}