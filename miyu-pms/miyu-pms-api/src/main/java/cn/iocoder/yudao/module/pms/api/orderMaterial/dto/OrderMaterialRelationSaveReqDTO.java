package cn.iocoder.yudao.module.pms.api.orderMaterial.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Schema(description = "订单物料更新表")
@Data
public class OrderMaterialRelationSaveReqDTO {

    @Schema(description = "id", requiredMode = Schema.RequiredMode.REQUIRED, example = "22368")
    private String id;

    @Schema(description = "订单id(本地关联时写入)", example = "31536")
    private String orderId;

    @Schema(description = "物料编码，牌号，毛坯(本地关联时写入)")
    private String materialCode;

    @Schema(description = "变码，工序加工后，产生的新码")
    private String variableCode;

    @Schema(description = "图号(成品工件,不一定是图号，成品码可能是别的码)")
    private String productCode;

    @Schema(description = "项目id", example = "29810")
    private String projectId;

    @Schema(description = "计划id", example = "24514")
    private String planId;

    @Schema(description = "子计划id", example = "647")
    private String planItemId;

    @Schema(description = "物料状态1,待分配,2，加工中3,未入库，4，外协中，5，加工完成,6待外协", example = "647")
    private Integer materialStatus;

}
