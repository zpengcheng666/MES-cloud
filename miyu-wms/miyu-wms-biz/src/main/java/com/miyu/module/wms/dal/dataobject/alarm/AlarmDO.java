package com.miyu.module.wms.dal.dataobject.alarm;

import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDataDO;
import lombok.*;
import java.util.*;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.*;
import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;

/**
 * 异常 DO
 *
 * @author QianJy
 */
@TableName("wms_alarm")
@KeySequence("wms_alarm_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AlarmDO extends BaseDataDO {

    /**
     * ID
     */
    @TableId(type = IdType.ASSIGN_ID)
    private String id;
    /**
     * 告警类型(1-系统,2-agv)
     *
     * 枚举 {@link TODO wms_alarm_type 对应的类}
     */
    private String alarmType;
    /**
     * 告警源
     */
    private String alarmSource;
    /**
     * 告警级别(1-信息,2-警告,3-错误)
     *
     * 枚举 {@link TODO wms_alarm_level 对应的类}
     */
    private String alarmLevel;
    /**
     * 告警编号
     */
    private String alarmNum;
    /**
     * 告警描述
     */
    private String alarmDesc;
    /**
     * 告警解除时间
     */
    private LocalDateTime alarmTime;
    /**
     * 异常状态,1-未解决,2-已解决,3-忽略
     *
     * 枚举 {@link TODO wms_alarm_state 对应的类}
     */
    private String alarmState;

}