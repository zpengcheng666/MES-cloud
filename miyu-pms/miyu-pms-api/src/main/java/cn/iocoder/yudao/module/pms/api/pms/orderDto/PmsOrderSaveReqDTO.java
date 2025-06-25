package cn.iocoder.yudao.module.pms.api.pms.orderDto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

@Schema(description = "订单数据接收类")
@Data
public class PmsOrderSaveReqDTO {

    @Schema(description = "id", requiredMode = Schema.RequiredMode.REQUIRED, example = "32706")
    private String id;

    @Schema(description = "项目编码(本地关联时写入)")
    private String projectCode;

    @Schema(description = "项目名称(本地关联时写入)")
    private String projectName;

    @Schema(description = "项目id(本地关联时写入)", example = "13694")
    private String projectId;

    @Schema(description = "物料牌号	产品编号(产品ID(与工艺内产品版本ID对应))		这里写的是material。在产品里是partNumber，图号。在物料里是material。")
    private String materialNumber;

    @Schema(description = "图号")
    private String partNumber;

    @Schema(description = "工件名称")
    private String partName;

    @Schema(description = "数量")
    private Integer quantity;

    @Schema(description = "带料加工(是1/否2)", example = "2")
    private Integer processType;

    @Schema(description = "订单状态(1,准备；2，生产；3,出库；4，关闭)", example = "2")
    private Integer orderStatus;

    @Schema(description = "订单类型(0,外部；1内部)", example = "1")
    private Integer orderType;

    @Schema(description = "物料状态,0为存在,1为不存在,不是表中字段")
    private String materialStatus;
    @Schema(description = "产品状态,0为存在,1为不存在,不是表中字段")
    private String productStatus;

    @Schema(description = "加工状态,精加工,粗加工等")
    private String processCondition;

    @Schema(description = "原料交付时间")
    private LocalDateTime materialDeliveryTime;

    @Schema(description = "成品交付时间")
    private LocalDateTime fproDeliveryTime;

}
