package cn.iocoder.yudao.module.pms.dal.dataobject.plan;

import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;
import com.baomidou.mybatisplus.annotation.*;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 项目计划子表，产品计划完善 DO
 *
 * @author 芋道源码
 */
@TableName("project_plan_purchase_material")
@KeySequence("project_plan_purchase_material_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PlanPurchaseMaterialDO extends BaseDO {

    /**
     * 项目计划id
     */
    @TableId(type = IdType.ASSIGN_ID)
    private String id;
    /**
     * 项目计划id
     */
    private String projectId;
    /**
     * 项目计划id
     */
    private String projectPlanId;
    /**
     * 项目子计划id
     */
    private String projectPlanItemId;
    /**
     * 计划类型
     */
    private Integer planType;

    /**
     * 项目订单id
     */
    private String projectOrderId;
    /**
     * 物料id
     */
    private String materialId;
    /**
     * 物料名
     */
    private String materialName;
    /**
     * 物料规格
     */
    private String materialSpecification;
    /**
     * 物料牌号(物料编号)
     */
    private String materialNumber;

    /**
     * 图号
     */
    private String partNumber;
    /**
     * 主计划数量(小于等于订单数量)
     */
    private Integer quantity;

    /**
     * 采购数量
     */
    private Integer purchaseAmount;

    /**
     * 零部件版本ID
     */
    private String partVersionId;
    /**
     * 使用库存
     */
    private BigDecimal useInventory;

    /**
     * 备注
     */
    private String remark;

    /**
     * 项目编号
     */
    private String projectCode;
    /**
     * 采购标记(0为未发起采购,1为已采购)
     */
    private Integer purchaseMark;
    /**
     * 采购时间
     */
    private LocalDateTime purchaseTime;
    /**
     * 预估单价
     */
    private BigDecimal predictPrice;

}
