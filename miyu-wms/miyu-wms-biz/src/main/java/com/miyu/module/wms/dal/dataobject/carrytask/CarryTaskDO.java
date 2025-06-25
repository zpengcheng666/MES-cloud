package com.miyu.module.wms.dal.dataobject.carrytask;

import lombok.*;
import java.util.*;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.*;
import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;

/**
 * 搬运任务 DO
 *
 * @author 技术部长
 */
@TableName("wms_carry_task")
@KeySequence("wms_carry_task_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CarryTaskDO extends BaseDO {

    /**
     * ID
     */
    @TableId(type = IdType.ASSIGN_ID)
    private String id;
    /**
     * 任务编码
     */
    private String taskCode;
    /**
     * 任务状态(未开始、进行中、已完成、已取消)
     *
     * 枚举 {@link TODO wms_carrying_task_status 对应的类}
     */
    private Integer taskStatus;
    /**
     * 任务类型(入库搬运、出库搬运、库存移交、呼叫托盘)
     *
     * 枚举 {@link TODO wms_carrying_task_type 对应的类}
     */
    private Integer taskType;
    /**
     * 优先级
     */
    private Integer priority;
    /**
     * 任务内容
     */
    private String taskContent;
    /**
     * 任务描述
     */
    private String taskDescription;
    /**
     * AGV ID
     */
    private String agvId;
    /**
     * 库存单集合
     */
    private String orderIds;

    /**
     * 反射使用 物料id
     */
    private String reflectStockId;

    /**
     * 反射使用 位置id
     */
    private String reflectLocationId;
    /**
     * 反射使用 仓库id
     */
    private String reflectWarehouseId;

    /**
     * 开始时间
     */
    private LocalDateTime startTime;
    /**
     * 结束时间
     */
    private LocalDateTime endTime;

    @TableField(exist = false)
    private List<CarrySubTaskDO> carrySubTask;

}