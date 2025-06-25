package com.miyu.cloud.mcs.dal.dataobject.distributionapplication;

import lombok.*;
import java.util.*;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.*;
import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;

/**
 * 物料配送申请 DO
 *
 * @author miyu
 */
@TableName("mcs_distribution_application")
@KeySequence("mcs_distribution_application_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DistributionApplicationDO extends BaseDO {

    /**
     * id
     */
    @TableId(type = IdType.ASSIGN_ID)
    private String id;
    /**
     * 申请单号
     */
    private String applicationNumber;
    /**
     * 申请单元
     */
    private String processingUnitId;
    /**
     * 任务订单id
     */
    private String orderId;
    /**
     * 任务订单编号
     */
    private String orderNumber;
    /**
     * 批次任务id
     */
    private String batchRecordId;
    /**
     * 批次任务编号
     */
    private String batchRecordNumber;
    /**
     * 申请状态
     */
    private Integer status;
    /**
     * 流程实例的编号
     */
    private String processInstanceId;

}
