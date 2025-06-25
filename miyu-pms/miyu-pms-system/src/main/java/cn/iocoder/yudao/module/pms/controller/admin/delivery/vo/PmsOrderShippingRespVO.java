package cn.iocoder.yudao.module.pms.controller.admin.delivery.vo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

@Schema(description = "发货")
@Data
public class PmsOrderShippingRespVO {

    @Schema(description = "订单id", requiredMode = Schema.RequiredMode.REQUIRED, example = "25686")
    private String id;

    @Schema(description = "项目编码(本地关联时写入)")
    private String projectCode;

    @Schema(description = "项目名称")
    private String projectName;

    @Schema(description = "项目编号", example = "17686")
    private String projectId;

    @Schema(description = "物料牌号")
    private String materialNumber;

    @Schema(description = "图号")
    private String partNumber;

    @Schema(description = "工件名称")
    private String partName;

    @Schema(description = "数量")
    private Integer quantity;


    @Schema(description = "带料加工(是/否)", example = "2")
    private Integer processType;

    @Schema(description = "订单状态(0,未开始,1,待审核,2,准备;3，生产;4,出库;5,关闭)", example = "2")
    private Integer orderStatus;

    @Schema(description = "订单类型(0,外部；1内部)", example = "1")
    private Integer orderType;

    @Schema(description = "加工状态", example = "2")
    private String processCondition;

    @Schema(description = "原料交付时间")
    private LocalDateTime materialDeliveryTime;

    @Schema(description = "成品交付时间")
    private LocalDateTime fproDeliveryTime;

    @Schema(description = "创建时间")
    private LocalDateTime createTime;

    private Integer materialStatus;
    private Integer productStatus;

    @Schema(description = "整单外协")
    private Integer outsource;

    /** 发货数量 */
    private Integer amount;
    /** 退货数量 */
    private Integer returnAmount;



}
