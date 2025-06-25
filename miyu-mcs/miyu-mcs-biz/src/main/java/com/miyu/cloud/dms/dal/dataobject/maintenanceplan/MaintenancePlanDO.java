package com.miyu.cloud.dms.dal.dataobject.maintenanceplan;

import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

import java.time.LocalDateTime;

/**
 * 设备保养维护计划 DO
 *
 * @author miyu
 */
@TableName("dms_maintenance_plan")
@KeySequence("dms_maintenance_plan_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MaintenancePlanDO extends BaseDO {

    /**
     * id
     */
    @TableId(type = IdType.ASSIGN_ID)
    private String id;
    /**
     * 计划编码
     */
    private String code;
    /**
     * 所属计划关联树
     */
    private String tree;
    /**
     * 设备
     */
    private String device;
    /**
     * 是否为关键设备
     */
    private Integer criticalDevice;
    /**
     * 启用状态
     */
    private Integer enableStatus;
    /**
     * 是否超期停机
     */
    private Integer expirationShutdown;
    /**
     * 超期时间
     */
    private Integer expirationTime;
    /**
     * 维护类型
     */
    private Integer type;
    /**
     * 开始时间
     */
    private LocalDateTime startTime;
    /**
     * cron表达式
     */
    private String cornExpression;
    /**
     * 计划任务id
     */
    private String jobId;
    /**
     * 维护内容
     */
    private String content;
    /**
     * 说明
     */
    private String remark;
    /**
     * 负责人
     */
    private String superintendent;
    /**
     * 最后一次保养时间
     */
    private LocalDateTime lastTime;
    /**
     * 上一次完成状态
     */
    private Integer lastStatus;

}
