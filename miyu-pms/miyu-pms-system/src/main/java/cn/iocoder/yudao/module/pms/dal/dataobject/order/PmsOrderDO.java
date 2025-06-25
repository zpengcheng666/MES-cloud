package cn.iocoder.yudao.module.pms.dal.dataobject.order;

import lombok.*;
import java.util.*;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.*;
import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;

/**
 * 项目订单 DO
 *
 * @author 芋道源码
 */
@TableName("project_order")
@KeySequence("project_order_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PmsOrderDO extends BaseDO {

    /**
     * 订单id
     */
    @TableId(type = IdType.ASSIGN_ID)
    private String id;
    /**
     * 项目编号
     */
    private String projectId;

    /**
     * 项目编码
     */
    private String projectCode;
    /**
     * 物料牌号(材质)
     */
    private String material;
    /**
     * 物料编码
     */
    private String materialNumber;
    /**
     * 图号
     */
    private String partNumber;
    /**
     * 工件名称
     */
    private String partName;
    /**
     * 数量
     */
    private Integer quantity;
    /**
     * 外协数量
     */
    private Integer outSourceAmount;
    /**
     * 工序外协
     */
    private Integer stepOutSourceAmount;
    /**
     * 带料加工(是/否)
     *
     * 枚举 {@link TODO pms_process_type 对应的类}
     */
    private Integer processType;
    /**
     * 订单id
     */
    private Integer orderStatus;
    /**
     * 订单类型
     */
    private Integer orderType;

    /**
     * 加工状态
     */
    private String processCondition;
    /**
     * 原料交付时间
     */
    private LocalDateTime materialDeliveryTime;
    /**
     * 成品交付时间
     */
    private LocalDateTime fproDeliveryTime;
    /**
     * 项目名
     */
    private String projectName;
    /**
     * 客户id
     */
    @TableField(exist = false)
    private String companyId;
    /**
     * 整单外协
     */
    private Integer outsource;
    /**
     * 整单外协备料
     */
    private Integer outsourcePrepareMaterial;

}
