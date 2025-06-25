package cn.iocoder.yudao.module.pms.api.orderMaterial.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Schema(description = "订单物料更新,主要查询字段是订单,子计划，物料状态，项目和计划几乎用不上")
@Data
public class OrderMaterialRelationReqListDTO {


    @Schema(description = "订单id(本地关联时写入)", example = "31536")
    private List<String> orderIds;

    @Schema(description = "项目id", example = "29810")
    private List<String> projectIds;

    @Schema(description = "计划id", example = "24514")
    private List<String> planIds;

    @Schema(description = "子计划id", example = "647")
    private List<String> planItemIds;

    @Schema(description = "物料状态1,待分配,2，加工中3,未入库，4，外协中，5，加工完成,6待外协", example = "647")
    private List<Integer> materialStatusList;

}
