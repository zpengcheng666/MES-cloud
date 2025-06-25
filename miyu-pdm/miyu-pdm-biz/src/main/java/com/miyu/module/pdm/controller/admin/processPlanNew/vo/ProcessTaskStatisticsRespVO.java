package com.miyu.module.pdm.controller.admin.processPlanNew.vo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;


@Schema(description = "pdm - 工艺任务统计信息 Response VO")
@Data
@ExcelIgnoreUnannotated
public class ProcessTaskStatisticsRespVO {


    @Schema(description = "id", example = "1")
    private String id;

    @Schema(description = "负责人")
    private String reviewedBy;

    @Schema(description = "负责人Id")
    private String reviewer;

    /**
     * one,two,three,five对应表中status状态：1待编制,2编制中,3审批中,5已定版
     */
    @Schema(description = "待编制")
    private String one;

    @Schema(description = "编制中")
    private String two;

    @Schema(description = "审批中")
    private String three;

    @Schema(description = "已定版")
    private String five;

    @Schema(description = "更新时间", requiredMode = Schema.RequiredMode.REQUIRED, example = "1024")
    private String[] updateTime;
}
