package com.miyu.cloud.mcs.dal.dataobject.batchorderdemand;

import lombok.*;
import java.util.*;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.*;
import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;

/**
 * 批次订单需求 DO
 *
 * @author miyu
 */
@TableName("mcs_batch_order_demand")
@KeySequence("mcs_batch_order_demand_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BatchOrderDemandDO extends BaseDO {

    /**
     * id
     */
    @TableId(type = IdType.ASSIGN_ID)
    private String id;
    /**
     * 订单id
     */
    private String orderId;
    /**
     * 订单编号
     */
    @TableField(exist = false)
    private String orderNumber;
    /**
     * 资源类型
     */
    private String resourceType;
    /**
     * 资源编码
     */
    private String resourceTypeCode;
    private String resourceTypeId;
    /**
     * 需求类别
     */
    private Integer requirementType;
    /**
     * 需求数量
     */
    private Integer total;
    /**
     * 齐备情况
     */
    private Integer status;
    /**
     * 确认人
     */
    private String confirmedBy;
    /**
     * 确认时间
     */
    private LocalDateTime confirmedTime;
    /**
     * 物料资源编码
     */
    private String materialCode;
    /**
     * 批量管理
     */
    private Boolean isBatch;
    /**
     * 备注
     */
    private String remarks;

}
