package cn.iocoder.yudao.module.pms.controller.admin.plan.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

@Schema(description = "工序外协采购计划接收类")
@Data
public class StepOutsourcePurchseReqVO {


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

    @Schema(description = "子计划id")
    private String planItemId;

    @Schema(description = "计划数量")
    private Integer quantity;

    @Schema(description = "零件图号")
    private String partNumber;

    @Schema(description = "项目编号")
    private String projectCode;

    private List<String> materialCodeList;

    private String materialCodeListStr;
}
