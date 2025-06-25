package com.miyu.module.pdm.controller.admin.feasibilityTask.vo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static cn.iocoder.yudao.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

@Schema(description = "管理后台 - 项目零件目录 Response VO")
@Data
@ExcelIgnoreUnannotated
public class FeasibilityTaskRespVO {

    @Schema(description = "评估任务id", example = "20041")
    private String id;

    @Schema(description = "项目编号")
    private String projectCode;

    @Schema(description = "零部件版本ID")
    private String partVersionId;

    @Schema(description = "负责人ID")
    private String reviewedBy;

    @Schema(description = "负责人姓名")
    private String reviewer;

    @Schema(description = "评估截止日期", requiredMode = Schema.RequiredMode.REQUIRED, example = "1024")
    private LocalDateTime deadline;

    @Schema(description = "状态")
    private String status;

    @Schema(description = "任务Id", example = "20041")
    private String taskId;

    @Schema(description = "创建时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime createTime;

    @Schema(description = "更新时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime updateTime;

    @Schema(description = "零件图号")
    private String partNumber;

    @Schema(description = "零件名称")
    private String partName;

    @Schema(description = "零件版次")
    private String partVersion;

    @Schema(description = "产品ID")
    private String rootProductId;

    @Schema(description = "产品图号")
    private String productNumber;

    @Schema(description = "客户化标识")
    private String customizedIndex;

    @Schema(description = "加工状态")
    private String processCondition;

}
