package com.miyu.module.es.dal.dataobject.visit;

import lombok.*;
import java.util.*;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.*;
import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;

/**
 * 访客记录 DO
 *
 * @author 上海弥彧
 */
@TableName("es_visit")
@KeySequence("es_visit_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VisitDO extends BaseDO {

//    /**
//     * 主键
//     */
//    @TableId(type = IdType.ASSIGN_ID)
//    private String id;

    /**
     * 访问记录ID
     */
    private String visitRecordId;
    /**
     * 访客姓名
     */
    private String name;
    /**
     * 访客签退时间
     */
    private LocalDateTime visitorCancelTime;
    /**
     * 访问记录状态(1：创建访客访问记录 2：取消访客访问记录 3：签到)
     *
     * 枚举 {@link TODO es_status 对应的类}
     */
    private Integer status;
    /**
     *访客单位
     */
    private String company;
    /**
     *来访目的
     */
    private String cause;
    /**
     *同行人数
     */
    private String followCount;
    /**
     * 计划开始时间
     */
    private LocalDateTime planBeginTime;
    /**
     * 计划结束时间
     */
    private LocalDateTime planEndTime;
    /**
     * 访客签到时间
     */
    private LocalDateTime visitorRecordTime;
    /**
     * 访客签到码
     */
    private String visitorCheckCode;
    /**
     * 被访人tpId
     */
    private String visitTpId;
    /**
     * 设备 SN
     */
    private String deviceSn;

}