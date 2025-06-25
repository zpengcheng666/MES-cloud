package com.miyu.cloud.mcs.dal.dataobject.batchdemandrecord;

import lombok.*;
import java.util.*;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.*;
import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;

/**
 * 需求分拣详情 DO
 *
 * @author miyu
 */
@TableName("mcs_batch_demand_record")
@KeySequence("mcs_batch_demand_record_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BatchDemandRecordDO extends BaseDO {

    /**
     * id
     */
    @TableId(type = IdType.ASSIGN_ID)
    private String id;
    /**
     * 物料id
     */
    private String materialUid;
    /**
     * 订单id
     */
    private String orderId;
    /**
     * 批次id
     */
    private String batchId;
    /**
     * 批次任务id
     */
    private String batchRecordId;
    /**
     * 生产单元
     */
    private String processingUnitId;
    /**
     * 生产设备
     */
    private String deviceId;
    /**
     * 需求id
     */
    private String demandId;
    /**
     * 物料类型id
     */
    private String materialConfigId;
    /**
     * 物料类型编号
     */
    private String materialNumber;
    /**
     * 物料条码
     */
    private String barCode;
    /**
     * 物料批次
     */
    private String batchNumber;
    /**
     * 数量
     */
    private Integer totality;
    /**
     * 是否为批量 1 单件 2 批量
     */
    private Integer batch;
    /**
     * 配送状态 0未配送 1已申请
     */
    private Integer status;
    /**
     * 物料类型 PROCESS_RESOURCES_TYPE_*
     */
    private String resourceType;
    /**
     * 任务计划开工时间
     */
    private LocalDateTime planStartTime;
    /**
     * 配送需求
     */
    private Boolean deliveryRequired;
    /**
     * 预生成配送编码
     */
    private String preDistributionNumber;

}
