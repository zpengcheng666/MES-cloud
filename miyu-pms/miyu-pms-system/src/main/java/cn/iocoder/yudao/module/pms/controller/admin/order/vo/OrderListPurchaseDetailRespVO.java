package cn.iocoder.yudao.module.pms.controller.admin.order.vo;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 项目订单表 采购详细
 *
 * @author 芋道源码
 */
@Data
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderListPurchaseDetailRespVO {
    private String id;
    /**
     * 产品编号(产品ID(与工艺内产品版本ID对应))
     */
    private String materialId;
    /**
     * 产品图号
     */
    private String partNumber;
    /**
     * 数量
     */
    private Integer quantity;
    /**
     * 销售订单编号
     */
    private String orderId;
    /**
     * 带料加工(是/否)
     *
     */
    private Integer processType;
    /**
     * 原料交付时间
     */
    private LocalDateTime materialDeliveryTime;
    /**
     * 成品交付时间
     */
    private LocalDateTime fproDeliveryTime;
    /**
     * 项目订单id
     */
    private String projectOrderId;

    /**
     * 签收数量
     */
    //private BigDecimal signAmount;
    /**
     * 退货数量
     */
    //private BigDecimal returnAmount;

    /**
     * 实际发货
     */
    private Integer actualAmount;

    /**
     * 进度
     */
    private BigDecimal progress;

    /**
     * 材料名
     */
    private String materialName;
    /**
     * 编号
     */
    private String materialNumber;

    /**
     * 未入库数量
     */
    private Integer remainAmount;
    /**
     * 合同id(采购单编号)
     */
    private String contractId;
    private String contractName;
    private String contractNumber;

}
