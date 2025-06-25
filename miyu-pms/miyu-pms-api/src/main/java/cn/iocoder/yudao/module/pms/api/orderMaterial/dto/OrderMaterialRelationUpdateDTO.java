package cn.iocoder.yudao.module.pms.api.orderMaterial.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Schema(description = "订单物料新增表")
@Data
public class OrderMaterialRelationUpdateDTO {

    @Schema(description = "id", requiredMode = Schema.RequiredMode.REQUIRED, example = "22368")
    private String id;

    @Schema(description = "订单id(本地关联时写入)", example = "31536")
//    @NotNull(message = "订单id不能为空")
    private String orderId;

    @Schema(description = "物料编码，牌号，毛坯(本地关联时写入)")
    private String materialCode;

    @Schema(description = "变码，工序加工后，产生的新码")
    private String variableCode;

    //新的工序码,用来替换之前的variableCode
//    @NotNull(message = "更新码不能为空")
    private String updateCode;

    @Schema(description = "图号(成品工件,不一定是图号，成品码可能是别的码)")
    private String productCode;

    @Schema(description = "生产订单编号")
    private String orderNumber;

    @Schema(description = "项目id", example = "29810")
    private String projectId;

    @Schema(description = "计划id", example = "24514")
    private String planId;

    @Schema(description = "子计划id", example = "647")
//    @NotNull(message = "子计划id不能为空")
    private String planItemId;

    @Schema(description = "物料状态", example = "647")
    private Integer materialStatus;

    @Schema(description = "工序顺序号", example = "647")
    private String step;

    @Schema(description = "合同id", example = "647")
    private String contractId;

    @Schema(description = "外协厂家", example = "647")
    private String aidMill;

}
