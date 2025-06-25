package com.miyu.module.wms.controller.admin.carrytask.vo;

import lombok.*;
import java.util.*;
import io.swagger.v3.oas.annotations.media.Schema;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;

import static cn.iocoder.yudao.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

@Schema(description = "管理后台 - 搬运任务分页 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class CarryTaskPageReqVO extends PageParam {

    @Schema(description = "创建时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] createTime;

    @Schema(description = "任务编码")
    private String taskCode;

    @Schema(description = "任务状态(未开始、进行中、已完成、已取消)", example = "2")
    private Integer taskStatus;

    @Schema(description = "任务类型(入库搬运、出库搬运、库存移交、呼叫托盘)", example = "1")
    private Integer taskType;

    @Schema(description = "任务内容")
    private String taskContent;

    @Schema(description = "任务描述", example = "随便")
    private String taskDescription;

    @Schema(description = "AGV ID", example = "25317")
    private String agvId;

    @Schema(description = "库存单集合")
    private String orderIds;

}