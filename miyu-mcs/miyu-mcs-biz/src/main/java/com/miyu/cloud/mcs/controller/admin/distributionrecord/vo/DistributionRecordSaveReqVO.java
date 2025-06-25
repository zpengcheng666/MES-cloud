package com.miyu.cloud.mcs.controller.admin.distributionrecord.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import javax.validation.constraints.*;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;

@Schema(description = "管理后台 - 物料配送申请详情新增/修改 Request VO")
@Data
public class DistributionRecordSaveReqVO {

    @Schema(description = "id", requiredMode = Schema.RequiredMode.REQUIRED, example = "21808")
    private String id;

    @Schema(description = "申请id", requiredMode = Schema.RequiredMode.REQUIRED, example = "11166")
    @NotEmpty(message = "申请id不能为空")
    private String applicationId;

    @Schema(description = "订单id", requiredMode = Schema.RequiredMode.REQUIRED, example = "868")
    @NotEmpty(message = "订单id不能为空")
    private String orderId;

    @Schema(description = "批次订单Id", requiredMode = Schema.RequiredMode.REQUIRED, example = "2984")
    @NotEmpty(message = "批次订单Id不能为空")
    private String batchId;

    @Schema(description = "需求Id")
    private String demandId;

    @Schema(description = "资源类型", example = "2")
    private String resourceType;

    @Schema(description = "资源编码")
    private String resourceTypeCode;

    private String resourceTypeId;

    private String processingUnitId;

    @Schema(description = "需求类别")
    private Integer requirementType;

    @Schema(description = "需求数量", example = "7728")
    private Integer count;

    @Schema(description = "配送状态")
    private Integer status;

    @Schema(description = "计划开工时间")
    private LocalDateTime planStartTime;

}
