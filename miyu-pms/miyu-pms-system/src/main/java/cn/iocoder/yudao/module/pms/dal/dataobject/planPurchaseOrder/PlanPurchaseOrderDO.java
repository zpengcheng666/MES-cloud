package cn.iocoder.yudao.module.pms.dal.dataobject.planPurchaseOrder;

import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;
import com.baomidou.mybatisplus.annotation.*;
import lombok.*;

/**
 * 项目计划 物料采购单
 *
 * @author 芋道源码
 */
@TableName("project_plan_purchase_order")
@KeySequence("project_plan_purchase_order_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PlanPurchaseOrderDO extends BaseDO {

    /**
     * id
     */
    @TableId(type = IdType.ASSIGN_ID)
    private String id;

    /**
     * 采购类型(0物料,1设备，2刀具，3工装，4刀具(部分)，5刀柄(部分))
     */
    private Integer type;


}
