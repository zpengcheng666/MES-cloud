package com.miyu.cloud.mcs.controller.admin.batchdemandrecord.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import javax.validation.constraints.*;

@Schema(description = "管理后台 - 需求分拣详情新增/修改 Request VO")
@Data
public class BatchDemandRecordSaveReqVO {

    @Schema(description = "id", requiredMode = Schema.RequiredMode.REQUIRED, example = "11150")
    private String id;

    @Schema(description = "物料id", example = "3360")
    private String materialUid;

    @Schema(description = "批次id", example = "3360")
    private String batchId;

    @Schema(description = "批次任务id")
    private String batchRecordId;

    @Schema(description = "需求id", example = "3360")
    private String demandId;

    @Schema(description = "物料类型id", example = "27367")
    private String materialConfigId;

    @Schema(description = "物料类型编号")
    private String materialNumber;

    @Schema(description = "物料条码")
    private String barCode;

    @Schema(description = "物料批次")
    private String batchNumber;

    @Schema(description = "数量")
    private Integer totality;

    @Schema(description = "物料管理模式，1：单件，2：批量", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer materialManage;

    @Schema(description = "是否为批量")
    private Integer batch;

    @Schema(description = "配送状态")
    private Integer status;

}
