package com.miyu.cloud.mcs.dal.dataobject.batchdetail;

import lombok.*;

import java.time.LocalDateTime;

import com.baomidou.mybatisplus.annotation.*;
import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;

/**
 * 批次详情 DO
 *
 * @author miyu
 */
@TableName("mcs_batch_detail")
@KeySequence("mcs_batch_detail_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BatchDetailDO1 extends BaseDO {

    /**
     * id
     */
    @TableId(type = IdType.ASSIGN_ID)
    private String id;
    /**
     * 订单id
     */
    private String batchId;
    /**
     * 详情id
     */
    private String orderDetailId;
    /**
     * 详情编码
     */
    private String orderDetailNumber;
    /**
     * 批次任务id
     */
    private String batchRecordId;
    /**
     * 物料条码
     */
    private String barCode;
    /**
     * 零件批次编号
     */
    private String partBatchNumber;

    /**
     * 生产单元
     */
    private String deviceUnitId;
    /**
     * 状态
     */
    private Integer status;
    /**
     * 开始时间
     */
    private LocalDateTime startTime;
    /**
     * 结束时间
     */
    private LocalDateTime endTime;
    /**
     * 收否检测
     */
    private Integer inspect;
    /**
     * 批量管理
     */
    private Boolean isBatch;
    /**
     * 数量
     */
    private Integer count;
    /**
     * 拆分父零件任务id
     */
    private String parentId;
    /**
     * 加工状态(1本厂 2外协)
     */
    private Integer procesStatus;
    /**
     * 外协厂家
     */
    private String aidMill;
    /**
     * 外协唯一码
     */
    private String outsourcingId;
    /**
     * 需求配送
     */
    private Integer needDelivery;

}
