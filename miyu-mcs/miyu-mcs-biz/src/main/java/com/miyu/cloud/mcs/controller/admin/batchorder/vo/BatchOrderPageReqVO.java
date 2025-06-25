package com.miyu.cloud.mcs.controller.admin.batchorder.vo;

import lombok.*;
import java.util.*;
import io.swagger.v3.oas.annotations.media.Schema;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;

import static cn.iocoder.yudao.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

@Schema(description = "管理后台 - 批次级订单分页 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class BatchOrderPageReqVO extends PageParam {

    @Schema(description = "ID", example = "1")
    private String id;

    @Schema(description = "订单编号")
    private String orderNumber;

    @Schema(description = "订单id")
    private String orderId;

    @Schema(description = "批次订单编码")
    private String batchNumber;

    @Schema(description = "工艺id")
    private String technologyId;
    /**
     * 工艺规程版本编码
     */
    private String technologyCode;

    @Schema(description = "起始顺序号")
    private String beginProcessId;

    @Schema(description = "拆单工序")
    private String splitProcessId;

    @Schema(description = "前置批次")
    private String preBatchId;

    @Schema(description = "前置批次编码")
    private String preBatchNumber;

    @Schema(description = "状态")
    private Integer status;

    private Integer submitStatus;

    @Schema(description = "批量管理")
    private Boolean isBatch;

    @Schema(description = "外协厂家")
    private String aidMill;

}
