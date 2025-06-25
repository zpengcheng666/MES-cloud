package com.miyu.cloud.mcs.dal.dataobject.batchrecordstep;

import lombok.*;
import java.util.*;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.*;
import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;

/**
 * 工步计划 DO
 *
 * @author 上海弥彧
 */
@TableName("mcs_batch_record_step")
@KeySequence("mcs_batch_record_step_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BatchRecordStepDO extends BaseDO {

    /**
     * id
     */
    @TableId(type = IdType.ASSIGN_ID)
    private String id;
    /**
     * 工序任务id
     */
    private String batchRecordId;
    /**
     * 工步id
     */
    private String stepId;
    /**
     * 工步名称
     */
    private String stepName;
    /**
     * 工步顺序号
     */
    private String stepOrder;
    /**
     * 设备类型
     */
    private String deviceTypeId;
    /**
     * 指定设备id集合
     */
    private String defineDeviceId;
    /**
     * 指定设备id集合
     */
    private String defineDeviceNumber;
    /**
     * 计划开始时间
     */
    private LocalDateTime planStartTime;
    /**
     * 计划结束时间
     */
    private LocalDateTime planEndTime;
    /**
     * 状态
     */
    private Integer status;
    /**
     * 计划开始时间
     */
    private LocalDateTime startTime;
    /**
     * 计划结束时间
     */
    private LocalDateTime endTime;

}
