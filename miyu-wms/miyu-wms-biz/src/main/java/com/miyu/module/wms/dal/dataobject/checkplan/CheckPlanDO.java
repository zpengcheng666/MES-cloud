package com.miyu.module.wms.dal.dataobject.checkplan;

import lombok.*;
import java.util.*;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.*;
import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;

/**
 * 库存盘点计划 DO
 *
 * @author QianJy
 */
@TableName("wms_check_plan")
@KeySequence("wms_check_plan_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CheckPlanDO extends BaseDO {

    /**
     * ID
     */
    @TableId(type = IdType.ASSIGN_ID)
    private String id;
    /**
     * 盘点库区id
     */
    private String checkAreaId;
    /**
     * 盘点名称
     */
    private String checkName;
    /**
     * 物料类型ids
     */
    private String materialConfigIds;
    /**
     * 开始时间
     */
    private LocalDateTime startTime;
    /**
     * 结束时间
     */
    private LocalDateTime endTime;
    /**
     * 截止时间
     */
    private LocalDateTime cutOffTime;
    /**
     * 负责人
     */
    private String checkUserId;
    /**
     * 盘点状态
     *
     * 枚举 {@link TODO check_status 对应的类}
     */
    private Integer checkStatus;
    /**
     * 是否锁盘
     *
     * 枚举 {@link TODO infra_boolean_string 对应的类}
     */
    private Boolean checkLocked;

    //存放指定容器
    @TableField(exist = false)
    private List<String> containerConfigNumbers;

    @TableField(exist = false)
    private String areaCode;
}