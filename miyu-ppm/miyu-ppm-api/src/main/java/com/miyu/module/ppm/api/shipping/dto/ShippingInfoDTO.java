package com.miyu.module.ppm.api.shipping.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Schema(description = "RPC 服务 - 销售系统  发货单信息")
@Data
public class ShippingInfoDTO {


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

    @Schema(description = "状态  0已创建 1审批中 2 待出库 3出库中4发货确认5结束 9作废8审批失败", example = "2")
    private Integer shippingStatus;

    @Schema(description = "发货单类型1销售发货2外协发货3采购退货4委托加工退货", example = "2")
    private Integer shippingType;

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

    @Schema(description = "出库状态  1 待出库  2代签收", example = "2")
    private Integer outStatus;
}
