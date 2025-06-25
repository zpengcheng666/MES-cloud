package com.miyu.cloud.mcs.controller.admin.orderform.vo;

import lombok.*;
import java.util.*;
import io.swagger.v3.oas.annotations.media.Schema;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;

import static cn.iocoder.yudao.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

@Schema(description = "管理后台 - 生产订单分页 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class OrderFormPageReqVO extends PageParam {

    @Schema(description = "id", example = "6308")
    private String orderId;

    @Schema(description = "主计划id")
    private String projectPlanId;

    @Schema(description = "项目号")
    private String projectNumber;

    @Schema(description = "订单编号")
    private String orderNumber;

    @Schema(description = "工艺规程版本Id", example = "123")
    private String technologyId;

    @Schema(description = "零件名称", example = "李四")
    private String partNumber;
    private String partVersionId;

    @Schema(description = "订单类型", example = "1")
    private Integer orderType;

    @Schema(description = "优先级")
    private Integer priority;

    @Schema(description = "订单状态")
    private Integer status;

    @Schema(description = "完成时间")
    private LocalDateTime completionTime;

    @Schema(description = "批量管理")
    private Boolean isBatch;

    @Schema(description = "物料编码集合")
    private String materialCode;

    @Schema(description = "加工状态(1本厂加工 2整单外协)")
    private Integer procesStatus;

    @Schema(description = "外协厂家")
    private String aidMill;

    @Schema(description = "下发状态")
    private Boolean issued;

    @Schema(description = "排产状态")
    private Integer schedulingStatus;

    @Schema(description = "首件")
    private Boolean first;
}
