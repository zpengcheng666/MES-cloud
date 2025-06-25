package com.miyu.module.qms.controller.admin.unqualifiedregistration.vo;

import lombok.*;
import io.swagger.v3.oas.annotations.media.Schema;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import java.math.BigDecimal;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;

import static cn.iocoder.yudao.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

@Schema(description = "管理后台 - 不合格品登记分页 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class UnqualifiedRegistrationPageReqVO extends PageParam {

    @Schema(description = "创建时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] createTime;

    @Schema(description = "缺陷描述")
    private String content;

    @Schema(description = "缺陷代码")
    private String defectiveCode;

    @Schema(description = "缺陷数量")
    private BigDecimal quantity;

    @Schema(description = "检验单方案任务ID", example = "13324")
    private String inspectionSheetSchemeId;

}