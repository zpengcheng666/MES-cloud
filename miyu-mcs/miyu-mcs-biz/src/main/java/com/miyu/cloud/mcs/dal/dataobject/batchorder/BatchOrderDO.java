package com.miyu.cloud.mcs.dal.dataobject.batchorder;

import com.miyu.cloud.mcs.dal.dataobject.batchrecord.BatchRecordDO;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

import com.baomidou.mybatisplus.annotation.*;
import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;

/**
 * 批次级订单 DO
 *
 * @author miyu
 */
@TableName("mcs_batch_order")
@KeySequence("mcs_batch_order_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BatchOrderDO extends BaseDO {

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
     * 批次订单编码
     */
    private String batchNumber;
    /**
     * 工艺规程版本Id
     */
    private String technologyId;
    /**
     * 工艺规程版本编码
     */
    private String technologyCode;
    /**
     * 起始顺序id
     */
    private String beginProcessId;
    /**
     * 拆单工序
     */
    private String splitProcessId;
    /**
     * 数量
     */
    private Integer count;
    /**
     * 预计开始时间
     */
    private LocalDateTime planStartTime;
    /**
     * 截止日期
     */
    private LocalDateTime planEndTime;
    /**
     * 开始时间
     */
    private LocalDateTime startTime;
    /**
     * 结束时间
     */
    private LocalDateTime endTime;
    /**
     * 前置批次
     */
    private String preBatchId;

    private Integer status;

    private Integer submitStatus;
    /**
     * 批量管理
     */
    private Boolean isBatch;

    /**
     * 加工类型(1本厂 2外协)
     */
    private Integer procesStatus;
    /**
     * 外协厂家
     */
    private String aidMill;
// TODO: 2025/2/8 绑定物料时, 更新配送的绑定关系与时间
    /**
     * 物料条码
     */
    private String barCode;

    /**
     * 当前所在加工单元
     */
    private String processingUnitName;
    /**
     * 外协识别码(唯一码)
     */
    private String outsourcingId;

    @TableField(exist = false)
    private String preBatchNumber;

    @TableField(exist = false)
    private String orderNumber;

    @TableField(exist = false)
    private String firstBatchId;

    @TableField(exist = false)
    private List<BatchRecordDO> detailList;
}
