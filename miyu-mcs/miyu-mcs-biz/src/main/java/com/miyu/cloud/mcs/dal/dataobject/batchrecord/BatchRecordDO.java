package com.miyu.cloud.mcs.dal.dataobject.batchrecord;

import lombok.*;

import java.time.LocalDateTime;
import java.util.*;
import com.baomidou.mybatisplus.annotation.*;
import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;

/**
 * 批次工序任务 DO
 *
 * @author 芋道源码
 */
@TableName("mcs_batch_record")
@KeySequence("mcs_batch_record_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BatchRecordDO extends BaseDO {

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
     * 批次id
     */
    private String batchId;
    /**
     * 工序Id
     */
    private String processId;
    /**
     * 编码
     */
    private String number;
    /**
     * 生产单元类型id
     */
    private String unitTypeIds;
    /**
     * 生产单元
     */
    private String processingUnitId;
    /**
     * 设备编号
     */
//    private String deviceNumber;
    /**
     * 设备id
     */
    private String deviceId;
    /**
     * 工序序号
     */
    private String procedureNum;
    /**
     * 状态
     */
    private Integer status;
    /**
     * 预计开始时间
     */
    private LocalDateTime planStartTime;
    /**
     * 预计结束时间
     */
    private LocalDateTime planEndTime;
    /**
     * 数量
     */
    private Integer count;
    /**
     * 前置批次Id
     */
    private String preRecordId;
    /**
     * 开始时间
     */
    private LocalDateTime startTime;
    /**
     * 结束时间
     */
    private LocalDateTime endTime;
    /**
     * 批量管理
     */
    private Boolean isBatch;
    /**
     * 加工状态(1本厂加工 2整单外协)
     */
    private Integer procesStatus;
    /**
     * 外协厂家
     */
    private String aidMill;

    /**
     * 物料条码
     */
    private String barCode;
    /**
     * 收否检测
     */
    private Integer inspect;
    /**
     * 需求配送
     */
    private Integer needDelivery;
    /**
     * 当前操作者
     */
    private String currentOperator;// todo 未维护
    /**
     * 外协识别码(唯一码)
     */
    private String outsourcingId;

}
