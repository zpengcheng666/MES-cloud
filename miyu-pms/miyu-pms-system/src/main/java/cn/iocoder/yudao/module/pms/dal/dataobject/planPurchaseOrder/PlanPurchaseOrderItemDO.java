package cn.iocoder.yudao.module.pms.dal.dataobject.planPurchaseOrder;

import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

/**
 * 项目计划 物料采购单
 *
 * @author 芋道源码
 */
@TableName("project_plan_purchase_order_item")
@KeySequence("project_plan_purchase_order_item_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PlanPurchaseOrderItemDO extends BaseDO {

    /**
     * id
     */
    @TableId(type = IdType.ASSIGN_ID)
    private String id;

    /**
     * 项目编码
     */
    private String projectCode;
    /**
     * 资源id
     */
    private String resourceId;
    /**
     * 物料采购单id
     */
    private String projectPlanPurchaseOrderId;


}
