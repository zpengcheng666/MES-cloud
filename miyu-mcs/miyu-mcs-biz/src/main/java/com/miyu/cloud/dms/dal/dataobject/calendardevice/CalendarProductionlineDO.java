package com.miyu.cloud.dms.dal.dataobject.calendardevice;

import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

/**
 * 设备日历关联部分,记录了设备绑定了那些班次 DO
 *
 * @author 上海弥彧
 */
@TableName("dms_calendar_productionline")
@KeySequence("dms_calendar_productionline_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CalendarProductionlineDO extends BaseDO {

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
     * 设备编号
     */
    private String deviceCode;
    /**
     * 设备名称
     */
    private String deviceName;
    /**
     * 班次id
     */
    private String shiftId;
    /**
     * 班次名称
     */
    private String shiftName;

}
