package com.miyu.cloud.mcs.dal.dataobject.productionrecords;

import lombok.*;
import java.util.*;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.*;
import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;

/**
 * 现场作业记录 DO
 *
 * @author miyu
 */
@TableName("mcs_production_records")
@KeySequence("mcs_production_records_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductionRecordsDO extends BaseDO {

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
     * 批次订单Id
     */
    private String batchId;
    /**
     * 批次任务id
     */
    private String batchRecordId;
    /**
     * 工艺id
     */
    private String technologyId;
    /**
     * 工序id
     */
    private String processId;
    /**
     * 工步id
     */
    private String stepId;
    /**
     * 生产单元id
     */
    private String processingUnitId;
    /**
     * 加工设备
     */
    private String equipmentId;
    /**
     * 订单编码
     */
    private String orderNumber;
    /**
     * 批次编码
     */
    private String batchNumber;
    /**
     * 工序任务编码
     */
    private String recordNumber;
    /**
     * 工序名称
     */
    private String processName;
    /**
     * 工步名称
     */
    private String stepName;
    /**
     * 单元名称
     */
    private String unitName;
    /**
     * 设备名称
     */
    private String deviceName;
    /**
     * 物料条码
     */
    private String barCode;
    /**
     * 操作类型
     */
    private Integer operationType;
    /**
     * 操作时间
     */
    private LocalDateTime operationTime;
    /**
     * 操作人
     */
    private String operationBy;
    /**
     * 数量
     */
    private Integer totality;
    /**
     * 任务进度
     */
    private Integer taskProgress;
    /**
     * 无工步任务
     */
    private Boolean noStep;

}
