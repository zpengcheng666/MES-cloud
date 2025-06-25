package com.miyu.cloud.mcs.controller.admin.distributionrecord.vo;

import cn.iocoder.yudao.framework.excel.core.annotations.DictFormat;
import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import java.util.*;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;
import com.alibaba.excel.annotation.*;

@Schema(description = "管理后台 - 物料配送申请详情 Response VO")
@Data
@ExcelIgnoreUnannotated
public class DistributionRecordRespVO {

    @Schema(description = "id", requiredMode = Schema.RequiredMode.REQUIRED, example = "21808")
    @ExcelProperty("id")
    private String id;

    @Schema(description = "申请id", requiredMode = Schema.RequiredMode.REQUIRED, example = "11166")
    @ExcelProperty("申请id")
    private String applicationId;

    @Schema(description = "子申请编码", requiredMode = Schema.RequiredMode.REQUIRED, example = "11166")
    @ExcelProperty("申请编码")
    private String number;

    @Schema(description = "需求Id")
    private String demandRecordId;

    @Schema(description = "任务id")
    private String batchRecordId;

    @Schema(description = "物料id")
    private String materialUid;

    @Schema(description = "物料类型id")
    private String materialConfigId;

    @Schema(description = "配送单元")
    private String processingUnitId;

    @Schema(description = "订单id", requiredMode = Schema.RequiredMode.REQUIRED, example = "868")
    @ExcelProperty("订单id")
    private String orderId;

    @Schema(description = "批次订单Id", requiredMode = Schema.RequiredMode.REQUIRED, example = "2984")
    @ExcelProperty("批次订单Id")
    private String batchId;

    @Schema(description = "需求Id")
    @ExcelProperty("需求Id")
    private String demandId;

    @Schema(description = "资源类型", example = "2")
    @ExcelProperty("资源类型")
    private String resourceType;

    @Schema(description = "资源编码")
    @ExcelProperty("资源编码")
    private String resourceTypeCode;
    private String resourceTypeId;

    @Schema(description = "物料批次")
    private String batchNumber;

    @Schema(description = "需求类别")
    @ExcelProperty("需求类别")
    private Integer requirementType;

    @Schema(description = "需求数量", example = "7728")
    @ExcelProperty("需求数量")
    private Integer count;

    @Schema(description = "配送状态")
    @DictFormat("mcs_delivery_record_status")
    private Integer status;

    @Schema(description = "计划开工时间")
    @ExcelProperty("计划开工时间")
    private LocalDateTime planStartTime;

    @Schema(description = "任务编码")
    private String BatchRecordNumber;

    @Schema(description = "单元名称")
    private String unitName;

    @Schema(description = "设备名称")
    private String deviceName;

    @Schema(description = "设备id")
    private String deviceId;

    @Schema(description = "配送起始位置")
    private String startLocationId;

    @Schema(description = "配送目标位置")
    private String targetLocationId;

    @Schema(description = "配送类型")
    @DictFormat("mcs_distribution_type")
    private Integer type;

    @Schema(description = "资源编码")
    private String materialNumber;

    @Schema(description = "条码")
    private String barCode;

    @Schema(description = "是否批量")
    private Integer batch;

    @Schema(description = "依据零件任务id")
    private String orderDetailId;

}
