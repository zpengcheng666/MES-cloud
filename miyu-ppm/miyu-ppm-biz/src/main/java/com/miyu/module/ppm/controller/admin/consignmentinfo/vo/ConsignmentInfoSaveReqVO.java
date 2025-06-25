package com.miyu.module.ppm.controller.admin.consignmentinfo.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import javax.validation.constraints.*;
import java.math.BigDecimal;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;

@Schema(description = "管理后台 - 收货产品新增/修改 Request VO")
@Data
public class ConsignmentInfoSaveReqVO {

    @Schema(description = "ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "9164")
    private String id;

    @Schema(description = "收货单ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "9599")

    private String consignmentId;

    @Schema(description = "合同ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "15670")

    private String contractId;

    @Schema(description = "合同订单ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "32718")

    private String orderId;

    @Schema(description = "物料类型ID", example = "28780")
    private String materialId;

    @Schema(description = "发货数量", requiredMode = Schema.RequiredMode.REQUIRED)

    private BigDecimal consignedAmount;

    @Schema(description = "签收数量")
    private BigDecimal signedAmount;

    @Schema(description = "签收人")
    private String signedBy;

    @Schema(description = "签收日期")
    private LocalDateTime signedTime;

    @Schema(description = "项目ID", example = "22515")
    private String projectId;

    @Schema(description = "项目订单ID", example = "5250")
    private String projectOrderId;

    @Schema(description = "项目计划ID", example = "19674")
    private String projectPlanId;

    @Schema(description = "项目子计划ID", example = "17838")
    private String projectPlanItemId;

    @Schema(description = "状态  状态  0已创建 1审批中 2待签收 3 入库中4待质检5质检中 6结束 7审批不通过 8已作废9待确认", example = "2")
    private Integer consignmentStatus;

    @Schema(description = "收货单类型1采购收货2外协收获3外协原材料退货4委托加工收货5销售退货", example = "2")
    private Integer consignmentType;

    private List<String> ids;

    private String locationId;

}