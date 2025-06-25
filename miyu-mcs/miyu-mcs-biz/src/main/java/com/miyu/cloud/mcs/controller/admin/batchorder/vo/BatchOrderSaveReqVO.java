package com.miyu.cloud.mcs.controller.admin.batchorder.vo;

import com.miyu.cloud.mcs.dal.dataobject.batchrecord.BatchRecordDO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import javax.validation.constraints.*;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;

@Schema(description = "管理后台 - 批次级订单新增/修改 Request VO")
@Data
public class BatchOrderSaveReqVO {

    @Schema(description = "ID", example = "1")
    private String id;

    @Schema(description = "订单id")
    private String orderId;

    @Schema(description = "订单编号", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotEmpty(message = "订单编号不能为空")
    private String orderNumber;

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

    @Schema(description = "数量", requiredMode = Schema.RequiredMode.REQUIRED, example = "23641")
    @NotNull(message = "数量不能为空")
    private Integer count;

    @Schema(description = "预计开始时间")
    private LocalDateTime planStartTime;

    @Schema(description = "截止日期")
    private LocalDateTime planEndTime;

    @Schema(description = "开始时间")
    private LocalDateTime startTime;

    @Schema(description = "结束时间")
    private LocalDateTime endTime;

    @Schema(description = "前置批次")
    private String preBatchId;

    @Schema(description = "前置批次编码")
    private String preBatchNumber;

    @Schema(description = "状态")
    private Integer status;

    private Integer submitStatus;

    private List<BatchRecordDO> detailList;

    @Schema(description = "批量管理")
    private Boolean isBatch;

    @Schema(description = "加工类型")
    private Integer procesStatus;

    @Schema(description = "外协厂家")
    private String aidMill;

    @Schema(description = "物料条码")
    private String barCode;
}
