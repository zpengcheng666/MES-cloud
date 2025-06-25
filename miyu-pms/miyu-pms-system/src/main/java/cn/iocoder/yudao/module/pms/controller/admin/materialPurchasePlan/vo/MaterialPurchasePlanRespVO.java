package cn.iocoder.yudao.module.pms.controller.admin.materialPurchasePlan.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;

@Schema(description = "管理后台 - 物料采购计划")
@Data
public class MaterialPurchasePlanRespVO {

    private String id;

    private String projectPlanId;
    /**
     * 图号
     */
    private String partNumber;
    /**
     * 主计划数量(小于等于订单数量)
     */
    private Integer quantity;
    /**
     * 项目id
     */
    private String ProjectId;
    /**
     * 项目名
     */
    private String ProjectName;
    /**
     * 项目编号
     */
    private String ProjectCode;
    /**
     * 物料id
     */
    private String materialId;
    /**
     * 物料名
     */
    private String materialName;
    /**
     * 物料退货状态
     */
    private BigDecimal materialRerurnAmount;
    /**
     * 物料库存
     */
    private BigDecimal inventory;
    /**
     * 使用库存
     */
    private BigDecimal useInventory;

    /**
     * 可用库存
     */
    private BigDecimal inventoryAvailable;

    /**
     * 工艺方案
     */
    private String processScheme;

}
