package com.miyu.module.ppm.api.purchaseConsignment.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Schema(description = "RPC 服务 - 采购系统  入库完成请求")
@Data
public class ConsignmentInfoDTO {


    @Schema(description = "ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "9164")
    private String id;

//    @Schema(description = "收货单ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "9599")
//    private String consignmentId;
    @Schema(description = "收货单编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "9599")
    private String no;
    @Schema(description = "收货单名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "9599")
    private String name;

    @Schema(description = "合同ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "15670")
    private String contractId;
    @Schema(description = "合同名", requiredMode = Schema.RequiredMode.REQUIRED, example = "15670")
    private String contractName;
    @Schema(description = "合同编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "15670")
    private String contractCode;

    @Schema(description = "公司名", requiredMode = Schema.RequiredMode.REQUIRED, example = "15670")
    private String companyName;
    private String companyId;




    @Schema(description = "合同订单ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "32718")
    private String orderId;

    @Schema(description = "物料类型ID", example = "28780")
    private String materialConfigId;

    @Schema(description = "发货数量", requiredMode = Schema.RequiredMode.REQUIRED)
    private BigDecimal consignedAmount;

    @Schema(description = "签收数量")
    private BigDecimal signedAmount;

    @Schema(description = "签收人")
    private String signedBy;

    @Schema(description = "签收日期")
    private LocalDateTime signedTime;

    @Schema(description = "创建日期", requiredMode = Schema.RequiredMode.REQUIRED)
    private LocalDateTime createTime;

    @Schema(description = "项目ID", example = "22515")
    private String projectId;

    private String projectName;

    private String projectCode;

    @Schema(description = "项目订单ID", example = "5250")
    private String projectOrderId;

    @Schema(description = "项目计划ID", example = "19674")
    private String projectPlanId;

    @Schema(description = "项目子计划ID", example = "17838")
    private String projectPlanItemId;

    @Schema(description = "状态  状态  0已创建 1审批中 2待签收 3 入库中4待质检5质检中 6结束 7审批不通过 8已作废9待确认10退货", example = "2")
    private Integer consignmentStatus;

    @Schema(description = "收货单类型1采购收货2外协收获3外协原材料退货4委托加工收货5销售退货", example = "2")
    private Integer consignmentType;

    @Schema(description = "物料编号")
    private String materialNumber;

    @Schema(description = "物料类码")
    private String materialCode;

    @Schema(description = "物料名称", example = "张三")
    private String materialName;

    @Schema(description = "物料属性（成品、毛坯、辅助材料）",requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer materialProperty;

    @Schema(description = "物料类型（零件、托盘、工装、夹具、刀具）",requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    private String materialType;

    @Schema(description = "物料规格")
    private String materialSpecification;

    @Schema(description = "物料品牌")
    private String materialBrand;

    @Schema(description = "物料单位")
    private String materialUnit;

}
