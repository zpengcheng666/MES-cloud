package cn.iocoder.yudao.module.pms.api.pms.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDateTime;

/**
 * 项目计划子表，产品计划完善 DO
 *
 * @author 芋道源码
 */

@Data
@Schema(description = "项目计划子表")
public class PlanItemDTO {

    /**
     * 项目计划id
     */
    private String id;
    /**
     * 项目计划id
     */
    private String projectPlanId;
    /**
     * 产品id
     */
    private String materialId;
    /**
     * 主计划数量(小于等于订单数量)
     */
    private Integer quantity;
    /**
     * 工艺准备完成时间
     */
    private LocalDateTime processPreparationTime;
    /**
     * 生产准备完成时间
     */
    private LocalDateTime productionPreparationTime;
    /**
     * 采购完成时间
     */
    private LocalDateTime purchaseCompletionTime;
    /**
     * 入库时间
     */
    private LocalDateTime warehousingTime;
    /**
     * 完成检验时间
     */
    private LocalDateTime checkoutCompletionTime;
    /**
     * 计划交付时间
     */
    private LocalDateTime planDeliveryTime;
    /**
     * 备注
     */
    private String remark;

}
