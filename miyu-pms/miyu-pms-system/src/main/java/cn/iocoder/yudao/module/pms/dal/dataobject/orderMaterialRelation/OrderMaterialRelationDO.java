package cn.iocoder.yudao.module.pms.dal.dataobject.orderMaterialRelation;

import lombok.*;
import java.util.*;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.*;
import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;

/**
 * 订单物料关系表 DO
 *
 * @author 上海弥彧
 */
@TableName("project_order_material_relation")
@KeySequence("project_order_material_relation_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderMaterialRelationDO extends BaseDO {

    /**
     * 订单id
     */
    @TableId(type = IdType.ASSIGN_ID)
    private String id;
    /**
     * 项目id(本地关联时写入)
     */
    private String orderId;
    /**
     * 物料编码，牌号，毛坯(本地关联时写入)
     */
    private String materialCode;
    /**
     * 物料类型id
     */
    private String materialTypeId;
    /**
     * 变码，工序加工后，产生的新码
     */
    private String variableCode;
    /**
     * 图号(成品工件)
     */
    private String productCode;
    /**
     * 项目id
     */
    private String projectId;
    /**
     * 计划id
     */
    private String planId;
    /**
     * 子计划id
     */
    private String planItemId;
    /**
     * 物料状态
     */
    private Integer materialStatus;
    /**
     * 计划类型
     */
    private Integer planType;
    /**
     * 是否通过备料选择,是为1，默认为0
     */
    private boolean prepare;

    @TableField(exist = false)
    private String updateCode;
    /**
     * 工序
     */
    private String step;

    /**
     * 合同id
     * 外协后才有
     */
    private String contractId;

    /**
     * 外协厂家
     * 外协后才有
     */
    private String aidMill;

    /**
     * 订单编码(生码)
     */
    private String orderNumber;

}
