package com.miyu.module.qms.controller.admin.inspectionsheetsamplingrule.vo;

import lombok.*;
import io.swagger.v3.oas.annotations.media.Schema;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;

import static cn.iocoder.yudao.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

@Schema(description = "管理后台 - 检验单抽样规则（检验抽样方案）关系分页 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class InspectionSheetSamplingRulePageReqVO extends PageParam {

    @Schema(description = "创建时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] createTime;

    @Schema(description = "检测任务ID", example = "32464")
    private String inspectionSheetSchemeId;

    @Schema(description = "检测项目ID", example = "21674")
    private String inspectionSchemeItemId;

    @Schema(description = "抽样方案ID", example = "23434")
    private String samplingRuleConfigId;

    @Schema(description = "抽样标准ID", example = "1128")
    private String samplingStandardId;

}