package com.miyu.cloud.dms.dal.dataobject.calendardevice;

import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;

/**
 * 设备日历,记录设备每天的可用时间 DO
 *
 * @author 上海弥彧
 */
@TableName("dms_calendar_device")
@KeySequence("dms_calendar_device_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CalendarDeviceDO extends BaseDO {

    /**
     * id
     */
    @TableId(type = IdType.ASSIGN_ID)
    private String id;
    /**
     * 设备id
     */
    private String deviceId;
    /**
     * 日期
     */
    private LocalDate date;
    /**
     * 开始时间
     */
    private LocalTime startTime;
    /**
     * 结束时间
     */
    private LocalTime endTime;

}
