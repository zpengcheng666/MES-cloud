package com.miyu.cloud.dms.dal.dataobject.calendarshift;

import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;
import com.baomidou.mybatisplus.annotation.*;
import lombok.*;

/**
 * 班次类型 DO
 *
 * @author 上海弥彧
 */
@TableName("dms_calendar_shift_type")
@KeySequence("dms_calendar_shift_type_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ShiftTypeDO extends BaseDO {

    /**
     * id
     */
    @TableId(type = IdType.ASSIGN_ID)
    private String id;
    /**
     * 班次名称
     */
    private String name;
    /**
     * 基础日历id
     */
    private String bcid;
    /**
     * 基础日历名
     */
    private String basicName;

    /**
     * 开始时间
     */
    @TableField(exist = false)
    private String startDate;
    /**
     * 结束时间
     */
    @TableField(exist = false)
    private String endDate;
    /**
     * 设备id
     */
    @TableField(exist = false)
    private String deviceId;

}
