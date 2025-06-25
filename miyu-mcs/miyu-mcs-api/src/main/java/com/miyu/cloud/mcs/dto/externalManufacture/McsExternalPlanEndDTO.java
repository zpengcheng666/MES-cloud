package com.miyu.cloud.mcs.dto.externalManufacture;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Data
public class McsExternalPlanEndDTO {

    //物料唯一码
    @Schema(description = "物料编码", requiredMode = Schema.RequiredMode.REQUIRED, example = "BC-1234")
    private String materialCode;
    //工序任务id
    @Schema(description = "工序任务id(id与编码必填一个)", example = "123456")
    private String orderId;
    //工序任务编码
    @Schema(description = "工序任务编码(id与编码必填一个)", example = "OR123")
    private String orderNumber;
    //工步序号
    @Schema(description = "工步序号", requiredMode = Schema.RequiredMode.REQUIRED, example = "15.01")
    private String stepNumber;
    //完成时间
    @Schema(description = "完成时间", example = "1735893547000")
    private LocalDateTime realEnd = LocalDateTime.now();
    //操作人
    @Schema(description = "操作人", requiredMode = Schema.RequiredMode.REQUIRED, example = "123456")
    private String realEndBy;
    //设备编码
    @Schema(description = "设备编码", requiredMode = Schema.RequiredMode.REQUIRED, example = "abc")
    private String deviceNumber;
    //起始进度 0-100 默认100
    @Schema(description = "起始进度 0-100 默认0", example = "0")
    private Integer progress = 100;

    //测量专用
    //测量结果
    @Schema(description = "测量结果")
    private McsMeasureResultDataDTO measureData;
    //测量结果文件名称
    @Schema(description = "测量结果文件名称")
    private List<String> reportFileName;

    //蒙拉专用
    //批次任务编码(实订单编码)
    @Schema(description = "批次任务编码", example = "OR123")
    private String batchNumber;
    //工序序号
    @Schema(description = "工序序号", example = "15")
    private String processNumber;

}
