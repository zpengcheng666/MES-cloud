package com.miyu.cloud.dms.dal.dataobject.calendar;

import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

/**
 * 基础日历的工作日 DO
 *
 * @author 上海弥彧
 */
@TableName("dms_calendar_detail")
@KeySequence("dms_calendar_detail_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CalendarDetailDO extends BaseDO {

    /**
     * id
     */
    @TableId(type = IdType.ASSIGN_ID)
    private String id;
    /**
     * 基础日历id
     */
    private String bcid;
    /**
     * 休息/工作(1/2)
     *
     * 枚举 {@link TODO calendar_status 对应的类}
     */
    private Integer cdname;
    /**
     * 日期
     */
    private String cddate;

}
