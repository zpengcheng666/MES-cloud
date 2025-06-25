package com.miyu.module.wms.dal.dataobject.carrytask;

import lombok.*;
import java.util.*;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.*;
import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;

/**
 * 搬运任务子表 DO
 *
 * @author 技术部长
 */
@TableName("wms_carry_sub_task")
@KeySequence("wms_carry_sub_task_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CarrySubTaskDO extends BaseDO {

    /**
     * ID
     */
    @TableId(type = IdType.ASSIGN_ID)
    private String id;
    /**
     * 指令类型(移动、取、放、上架、下架)
     *
     * 枚举 {@link TODO wms_instruction_type 对应的类}
     */
    private Integer insType;
    /**
     * 任务状态（未开始、进行中、已完成、已取消）
     *
     * 枚举 {@link TODO wms_carrying_task_status 对应的类}
     */
    private Integer taskStatus;
    /**
     * 物料id
     */
    private String materialStockId;
    /**
     * 库位id
     */
    private String locationId;
    /**
     * 执行顺序
     */
    private Integer executeOrder;
    /**
     * 搬运任务id
     */
    private String parentId;
    /**
     * 任务内容
     */
    private String taskContent;
    /**
     * 任务描述
     */
    private String taskDescription;
    /**
     * 开始时间
     */
    private LocalDateTime startTime;
    /**
     * 结束时间
     */
    private LocalDateTime endTime;

    /**
     * 上下架指令id
     */
    private String instructionId;

    /**
     * 触发事件
     */
    private String trigEvent;

    /**
     * 特殊标识(表示此子任务开始即为托盘回库任务)
     */
    private Boolean specialMark;

    @TableField(exist = false)
    private String locationCode;

    @TableField(exist = false)
    private String barCode;

}
