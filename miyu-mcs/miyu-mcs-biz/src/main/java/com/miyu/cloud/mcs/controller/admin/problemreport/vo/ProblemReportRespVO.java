package com.miyu.cloud.mcs.controller.admin.problemreport.vo;

import cn.iocoder.yudao.framework.excel.core.annotations.DictFormat;
import cn.iocoder.yudao.framework.excel.core.convert.DictConvert;
import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

@Schema(description = "管理后台 - 问题上报 Response VO")
@Data
@ExcelIgnoreUnannotated
public class ProblemReportRespVO {

    @Schema(description = "id", requiredMode = Schema.RequiredMode.REQUIRED, example = "3993")
    @ExcelProperty("id")
    private String id;

    @Schema(description = "工位id")
    @ExcelProperty("工位id")
    private String stationId;

    @Schema(description = "问题类型", example = "1")
    @ExcelProperty(value = "问题类型", converter = DictConvert.class)
    @DictFormat("mcs_problem_report_type") // TODO 代码优化：建议设置到对应的 DictTypeConstants 枚举类中
    private Integer type;

    @Schema(description = "上报id", example = "14910")
    @ExcelProperty("上报id")
    private String reportId;

    @Schema(description = "状态", example = "1")
    @ExcelProperty("状态")
    private String status;

    @Schema(description = "问题描述")
    @ExcelProperty("问题描述")
    private String content;

    @Schema(description = "创建时间")
    @ExcelProperty("创建时间")
    private LocalDateTime createTime;

}