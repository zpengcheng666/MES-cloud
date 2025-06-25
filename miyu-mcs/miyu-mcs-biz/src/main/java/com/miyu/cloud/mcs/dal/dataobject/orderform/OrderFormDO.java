package com.miyu.cloud.mcs.dal.dataobject.orderform;

import lombok.*;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.*;
import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;

/**
 * 生产订单 DO
 *
 * @author miyu
 */
@TableName("mcs_order_form")
@KeySequence("mcs_order_form_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderFormDO extends BaseDO {

    /**
     * id
     */
    @TableId(type = IdType.ASSIGN_ID)
    private String id;
    /**
     * 主计划id
     */
    private String projectPlanId;
    /**
     * 订单编号
     */
    private String orderNumber;
    /**
     * 项目号
     */
    private String projectNumber;
    /**
     * 零件图号id
     */
    private String partVersionId;
    /**
     * 零件图号
     */
    private String partNumber;
    /**
     * 工艺规程版本Id
     */
    private String technologyId;
    /**
     * 起始工序号
     */
    private String beginProcessNumber;
    /**
     * 订单类型
     */
    private Integer orderType;
    /**
     * 优先级
     */
    private Integer priority;
    /**
     * 数量
     */
    private Integer count;
    /**
     * 接收时间
     */
    private LocalDateTime receptionTime;
    /**
     * 交付时间
     */
    private LocalDateTime deliveryTime;
    /**
     * 完成时间
     */
    private LocalDateTime completionTime;
    /**
     * 负责人
     */
    private String responsiblePerson;
    /**
     * 订单状态
     */
    private Integer status;
    /**
     * 批量管理
     */
    private Boolean isBatch;
    /**
     * 物料编码集合
     */
    private String materialCode;
    /**
     * 加工状态(1本厂加工 2整单外协)
     */
    private Integer procesStatus;
    /**
     * 外协厂家
     */
    private String aidMill;
    /**
     * 下发状态
     */
    private Boolean issued;
    /**
     * 排产状态
     */
    private Integer schedulingStatus;
    /**
     * 首件
     */
    private Boolean first;
}
