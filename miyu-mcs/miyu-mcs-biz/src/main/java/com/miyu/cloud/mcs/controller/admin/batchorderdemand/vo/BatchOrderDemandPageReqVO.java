package com.miyu.cloud.mcs.controller.admin.batchorderdemand.vo;

import lombok.*;
import java.util.*;
import io.swagger.v3.oas.annotations.media.Schema;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;

import static cn.iocoder.yudao.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

@Schema(description = "管理后台 - 批次订单需求分页 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class BatchOrderDemandPageReqVO extends PageParam {

    @Schema(description = "ID", example = "1")
    private String id;

    private String orderId;

    @Schema(description = "订单编号")
    private String orderNumber;

    @Schema(description = "资源类型", example = "2")
    private String resourceType;

    @Schema(description = "资源编码")
    private String resourceTypeCode;
    private String resourceTypeId;

    @Schema(description = "需求类别")
    private Integer requirementType;

    @Schema(description = "齐备情况", example = "1")
    private Integer status;

}
