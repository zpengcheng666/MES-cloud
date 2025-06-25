package com.miyu.module.qms.controller.admin.inspectiontoolverificationrecord.vo;

import lombok.*;
import java.util.*;
import io.swagger.v3.oas.annotations.media.Schema;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;

import static cn.iocoder.yudao.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

@Schema(description = "管理后台 - 检验工具校准记录分页 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class InspectionToolVerificationRecordPageReqVO extends PageParam {

    @Schema(description = "创建时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] createTime;

    @Schema(description = "库存主键", example = "4249")
    private String stockId;

    @Schema(description = "工具名称", example = "13328")
    private String toolName;

    @Schema(description = "送检日期")
    private String verificationDateBegin;

    @Schema(description = "时间送检日期")
    private String verificationDateBeginAct;

    @Schema(description = "完成时间")
    private String verificationDateEnd;

    @Schema(description = "状态", example = "1")
    private Integer status;

}
