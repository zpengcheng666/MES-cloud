package com.miyu.cloud.mcs.controller.admin.receiptrecord.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import javax.validation.constraints.*;

@Schema(description = "管理后台 - 生产单元签收记录新增/修改 Request VO")
@Data
public class ReceiptRecordSaveReqVO {

    @Schema(description = "id", requiredMode = Schema.RequiredMode.REQUIRED, example = "23634")
    private String id;

    @Schema(description = "申请id", requiredMode = Schema.RequiredMode.REQUIRED, example = "7789")
    @NotEmpty(message = "申请id不能为空")
    private String applicationId;

    @Schema(description = "需求详情id")
    private String demandRecordId;

    @Schema(description = "物料id")
    private String materialUid;

    @Schema(description = "物料批次编码")
    private String batchNumber;

    @Schema(description = "物料条码")
    private String barCode;

    @Schema(description = "申请编码")
    private String applicationNumber;;

    @Schema(description = "申请单元", example = "242")
    private String processingUnitId;

    @Schema(description = "申请单元名称")
    private String unitName;

    @Schema(description = "资源类型", example = "2")
    private String resourceType;

    @Schema(description = "资源类码")
    private String resourceCode;

    @Schema(description = "资源编码")
    private String resource;

    @Schema(description = "接收数量", example = "29024")
    private Integer count;

}
