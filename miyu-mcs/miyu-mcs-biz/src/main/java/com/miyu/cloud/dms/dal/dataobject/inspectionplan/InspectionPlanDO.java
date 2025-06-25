package com.miyu.cloud.dms.dal.dataobject.inspectionplan;

import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

import java.time.LocalDateTime;

/**
 * 设备检查计划 DO
 *
 * @author miyu
 */
@TableName("dms_inspection_plan")
@KeySequence("dms_inspection_plan_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InspectionPlanDO extends BaseDO {

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
     * 启用状态
     * <p>
     * 枚举 {@link TODO enableStatus 对应的类}
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
     * 检查类型
     * <p>
     * 枚举 {@link TODO dms_inspection_type 对应的类}
     */
    private Integer type;
    /**
     * 负责人
     */
    private String superintendent;
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
     * 检查内容
     */
    private String content;
    /**
     * 说明
     */
    private String remark;

}
