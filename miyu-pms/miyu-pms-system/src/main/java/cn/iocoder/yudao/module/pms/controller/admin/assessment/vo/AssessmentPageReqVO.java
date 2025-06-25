package cn.iocoder.yudao.module.pms.controller.admin.assessment.vo;

import lombok.*;
import java.util.*;
import io.swagger.v3.oas.annotations.media.Schema;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;

import static cn.iocoder.yudao.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

@Schema(description = "管理后台 - 项目评审分页 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class AssessmentPageReqVO extends PageParam {

    @Schema(description = "项目id", example = "17002")
    private String projectId;

    @Schema(description = "状态(未开启、评估中、评估结束)", example = "1")
    private Integer assessmentStatus;

    @Schema(description = "发起原因")
    private Integer reasonType;

    @Schema(description = "结论")
    private Integer conclusion;

    @Schema(description = "创建时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] createTime;

}
