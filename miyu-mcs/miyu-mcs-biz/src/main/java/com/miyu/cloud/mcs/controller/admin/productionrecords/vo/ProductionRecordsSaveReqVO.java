package com.miyu.cloud.mcs.controller.admin.productionrecords.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import javax.validation.constraints.*;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;

@Schema(description = "管理后台 - 现场作业记录新增/修改 Request VO")
@Data
public class ProductionRecordsSaveReqVO {

    @Schema(description = "id", requiredMode = Schema.RequiredMode.REQUIRED, example = "6199")
    private String id;

    @Schema(description = "订单id", requiredMode = Schema.RequiredMode.REQUIRED, example = "6308")
    @NotEmpty(message = "订单id不能为空")
    private String orderId;

    @Schema(description = "订单编号", requiredMode = Schema.RequiredMode.REQUIRED)
    private String orderNumber;

    @Schema(description = "批次订单Id", requiredMode = Schema.RequiredMode.REQUIRED, example = "18418")
    @NotEmpty(message = "批次订单Id不能为空")
    private String batchId;

    @Schema(description = "批次订单编码")
    private String batchCode;

    @Schema(description = "生产单元编号")
    private String processingUnitId;

    @Schema(description = "物料条码")
    private String barCode;

    @Schema(description = "加工设备", example = "31011")
    private String equipmentId;

    @Schema(description = "操作类型", example = "2")
    private Integer operationType;

    @Schema(description = "操作时间")
    private LocalDateTime operationTime;

    @Schema(description = "操作人")
    private String operationBy;

    @Schema(description = "数量")
    private String totality;

    @Schema(description = "订单零件id")
    private String orderDetailId;

    @Schema(description = "批次零件id")
    private String batchDetailId;

    @Schema(description = "批次任务id")
    private String batchRecordId;

    @Schema(description = "工序id")
    private String processId;

    @Schema(description = "工步id")
    private String stepId;

    @Schema(description = "批次编码")
    private String batchNumber;

    @Schema(description = "订单零件编码")
    private String orderDetailNumber;

    @Schema(description = "批次零件编码")
    private String batchDetailNumber;

    @Schema(description = "工序名称")
    private String processName;

    @Schema(description = "工步名称")
    private String stepName;

    @Schema(description = "单元名称")
    private String unitName;

    @Schema(description = "设备名称")
    private String deviceName;

}
