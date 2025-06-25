package com.miyu.cloud.mcs.controller.admin.distributionapplication.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import javax.validation.constraints.*;
import com.miyu.cloud.mcs.dal.dataobject.distributionrecord.DistributionRecordDO;

@Schema(description = "管理后台 - 物料配送申请新增/修改 Request VO")
@Data
public class DistributionApplicationSaveReqVO {

    @Schema(description = "id", requiredMode = Schema.RequiredMode.REQUIRED, example = "10728")
    private String id;

    @Schema(description = "申请单号")
    private String applicationNumber;

    @Schema(description = "申请单元", example = "6470")
    private String processingUnitId;

    @Schema(description = "订单id", example = "6470")
    private String orderId;

    @Schema(description = "订单编号", example = "6470")
    private String orderNumber;

    @Schema(description = "任务id", example = "6470")
    private String batchRecordId;

    @Schema(description = "任务编号", example = "6470")
    private String batchRecordNumber;

    @Schema(description = "申请状态", example = "1")
    private Integer status;

    @Schema(description = "物料配送申请详情列表")
    private List<DistributionRecordDO> distributionRecords;

}
