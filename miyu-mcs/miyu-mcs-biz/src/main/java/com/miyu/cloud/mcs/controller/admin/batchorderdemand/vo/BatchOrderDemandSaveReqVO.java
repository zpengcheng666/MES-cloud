package com.miyu.cloud.mcs.controller.admin.batchorderdemand.vo;

import com.miyu.cloud.mcs.controller.admin.batchdemandrecord.vo.BatchDemandResourceReqVO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import javax.validation.constraints.*;
import java.time.LocalDateTime;

@Schema(description = "管理后台 - 批次订单需求新增/修改 Request VO")
@Data
public class BatchOrderDemandSaveReqVO {

    @Schema(description = "ID", example = "1")
    private String id;

    @NotEmpty(message = "订单编号不能为空")
    @Schema(description = "订单id", example = "1")
    private String orderId;

    @Schema(description = "批次id", example = "1")
    private String batchId;

    @Schema(description = "工序任务id", example = "1")
    private String batchRecordId;

    @Schema(description = "订单编号", requiredMode = Schema.RequiredMode.REQUIRED)
    private String orderNumber;

    @Schema(description = "资源类型", example = "2")
    private String resourceType;

    @Schema(description = "资源编码")
    private String resourceTypeCode;
    private String resourceTypeId;

    @Schema(description = "需求类别")
    private Integer requirementType;

    @Schema(description = "需求数量")
    private Integer total;

    @Schema(description = "齐备情况", example = "1")
    private Integer status;

    @Schema(description = "确认人")
    private String confirmedBy;

    @Schema(description = "确认时间")
    private LocalDateTime confirmedTime;

    @Schema(description = "指定物料集合")
    private List<BatchDemandResourceReqVO> batchDemandResourceList;

    @Schema(description = "生产单元")
    private String processingUnitId;

    @Schema(description = "生产设备")
    private String deviceId;

    @Schema(description = "备注")
    private String remarks;

    @Schema(description = "资源id")
    private String resourceIds;

}
