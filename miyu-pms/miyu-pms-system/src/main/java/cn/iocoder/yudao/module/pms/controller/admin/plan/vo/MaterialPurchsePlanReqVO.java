package cn.iocoder.yudao.module.pms.controller.admin.plan.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema(description = "物料采购计划接收类")
@Data
public class MaterialPurchsePlanReqVO {


    @Schema(description = "采购数量")
    private Integer purchaseAmount;

    @Schema(description = "物料牌号")
    private String materialNumber;

    @Schema(description = "订单id")
    private String projectOrderId;

    @Schema(description = "计划类型")
    private Integer planType;

    @Schema(description = "项目id")
    private String projectId;

    @Schema(description = "计划id")
    private String projectPlanId;

    @Schema(description = "计划数量")
    private Integer quantity;

    @Schema(description = "零件图号")
    private String partNumber;

    @Schema(description = "项目编号")
    private String projectCode;
}
