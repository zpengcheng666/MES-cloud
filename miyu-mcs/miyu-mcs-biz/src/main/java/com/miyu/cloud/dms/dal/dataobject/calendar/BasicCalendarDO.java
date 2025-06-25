package com.miyu.cloud.dms.dal.dataobject.calendar;

import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

import java.time.LocalDateTime;

/**
 * 基础日历 DO
 *
 * @author 上海弥彧
 */
@TableName("dms_basic_calendar")
@KeySequence("dms_basic_calendar_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BasicCalendarDO extends BaseDO {

    /**
     * id
     */
    @TableId(type = IdType.ASSIGN_ID)
    private String id;
    /**
     * 日历名称
     */
    private String name;
    /**
     * 开始日期
     */
    private LocalDateTime startDate;
    /**
     * 结束日期
     */
    private LocalDateTime endDate;

}
