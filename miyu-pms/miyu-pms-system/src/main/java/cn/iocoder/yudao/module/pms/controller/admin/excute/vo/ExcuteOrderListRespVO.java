package cn.iocoder.yudao.module.pms.controller.admin.excute.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDateTime;

/**
 * 项目订单表子 DO
 *
 * @author 芋道源码
 */
@Data
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExcuteOrderListRespVO {
    /**
     * 产品编号(产品ID(与工艺内产品版本ID对应))
     */
    private String materialId;
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
     * 合同id
     */
    private String contractId;

    /**
     * 物料编号
     */
    private String materialNumber;

    /**
     * 成品物料编号
     */
    private String partNumber;

    /**
     * 物料类码
     */
    private String materialCode;

    /**
     * 物料名称
     */
    private String materialName;

    /**
     * 物料单位
     */
    private String materialUnit;

}
