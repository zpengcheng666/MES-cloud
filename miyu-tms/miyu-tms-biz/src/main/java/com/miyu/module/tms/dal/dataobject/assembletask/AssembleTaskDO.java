package com.miyu.module.tms.dal.dataobject.assembletask;

import lombok.*;
import java.util.*;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.*;
import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;

/**
 * 刀具装配任务 DO
 *
 * @author QianJy
 */
@TableName("tms_assemble_task")
@KeySequence("tms_assemble_task_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AssembleTaskDO extends BaseDO {

    /**
     * ID
     */
    @TableId(type = IdType.ASSIGN_ID)
    private String id;
    /**
     * 工单号
     */
    private String orderNumber;
    /**
     * 需求数量
     */
    private Integer needCount;
    /**
     * 目标位置
     */
    private String targetLocation;
    @TableField(exist = false)
    private String targetLocationCode;
    /**
     * 配送截止时间
     */
    private LocalDateTime distributionDeadline;
    /**
     * 物料类型id
     */
    private String materialConfigId;
    @TableField(exist = false)
    private String materialNumber;
    /**
     * 最短加工时长
     */
    private Integer minimumTime;
    /**
     * 状态（启用、作废）
     *
     * 枚举 {@link TODO infra_boolean_string 对应的类}
     */
    private Integer status;

    /**
     * 接单人
     */
    private String operator;
}