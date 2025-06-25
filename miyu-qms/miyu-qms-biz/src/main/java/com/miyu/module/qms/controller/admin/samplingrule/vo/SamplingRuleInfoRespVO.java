package com.miyu.module.qms.controller.admin.samplingrule.vo;

import cn.iocoder.yudao.framework.excel.core.annotations.DictFormat;
import cn.iocoder.yudao.framework.excel.core.convert.DictConvert;
import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

@Schema(description = "管理后台 - 抽样规则 Response VO")
@Data
@ExcelIgnoreUnannotated
public class SamplingRuleInfoRespVO {

    @Schema(description = "批量下限值（N）")
    @ExcelProperty("批量下限值（N）")
    private Integer minValue;

    @Schema(description = "批量上限值（N）")
    @ExcelProperty("批量上限值（N）")
    private Integer maxValue;


    @Schema(description = "样本数字码", requiredMode = Schema.RequiredMode.REQUIRED)
    private String sampleSizeCode1;
    @Schema(description = "样本数字码", requiredMode = Schema.RequiredMode.REQUIRED)
    private String sampleSizeCode2;
    @Schema(description = "样本数字码", requiredMode = Schema.RequiredMode.REQUIRED)
    private String sampleSizeCode3;
    @Schema(description = "样本数字码", requiredMode = Schema.RequiredMode.REQUIRED)
    private String sampleSizeCode4;
    @Schema(description = "样本数字码", requiredMode = Schema.RequiredMode.REQUIRED)
    private String sampleSizeCode5;
    @Schema(description = "样本数字码", requiredMode = Schema.RequiredMode.REQUIRED)
    private String sampleSizeCode6;
    @Schema(description = "样本数字码", requiredMode = Schema.RequiredMode.REQUIRED)
    private String sampleSizeCode7;



}