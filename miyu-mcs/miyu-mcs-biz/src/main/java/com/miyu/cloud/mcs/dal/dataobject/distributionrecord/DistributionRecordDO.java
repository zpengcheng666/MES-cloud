package com.miyu.cloud.mcs.dal.dataobject.distributionrecord;

import lombok.*;
import java.util.*;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.*;
import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;

/**
 * 物料配送申请详情 DO
 *
 * @author miyu
 */
@TableName("mcs_distribution_record")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DistributionRecordDO extends BaseDO {

    /**
     * id
     */
    @TableId(type = IdType.ASSIGN_ID)
    private String id;
    /**
     * 申请id
     */
    private String applicationId;
    /**
     * 子申请编码
     */
    private String number;
    /**
     * 订单id
     */
    private String demandRecordId;
    /**
     * 批次任务id
     */
    private String batchRecordId;
    /**
     * 物料id
     */
    private String materialUid;
    /**
     * 物料类型id
     */
    private String materialConfigId;
    /**
     * 配送单元
     */
    private String processingUnitId;
    /**
     * 配送设备
     */
    private String deviceId;
    /**
     * 配送起始位置
     */
    private String startLocationId;
    /**
     * 起始仓库
     */
    private String startWarehouseId;
    /**
     * 配送目标位置
     */
    private String targetLocationId;
    /**
     * 目标仓库
     */
    private String targetWarehouseId;
    /**
     * 资源类型
     */
    private String resourceType;
    /**
     * 配送类型
     */
    private Integer type;
    /**
     * 资源编码
     */
    private String materialNumber;
    /**
     * 物料批次码
     */
    private String batchNumber;
    /**
     * 条码
     */
    private String barCode;
    /**
     * 是否批量
     */
    private Integer batch;
    /**
     * 需求数量
     */
    private Integer count;
    /**
     * 配送状态
     */
    private Integer status;
    /**
     * 计划开工时间
     */
    private LocalDateTime planStartTime;

    @TableField(exist = false)
    private String operatorId;

    @TableField(exist = false)
    private LocalDateTime time;
}
