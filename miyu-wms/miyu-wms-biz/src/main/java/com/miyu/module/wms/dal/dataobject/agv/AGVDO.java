package com.miyu.module.wms.dal.dataobject.agv;

import lombok.*;
import java.util.*;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.*;
import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;

/**
 * AGV 信息 DO
 *
 * @author 上海弥彧
 */
@TableName("wms_agv")
@KeySequence("wms_agv_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AGVDO extends BaseDO {

    /**
     * ID
     */
    @TableId(type = IdType.ASSIGN_ID)
    private String id;

    /**
     * AGV 编号
     */
    private String carNo;

    /**
     * 机器人运行模式，手动模式=0，自动	模式=1
     *
     * 枚举 {@link TODO wms_mode 对应的类}
     */
    private Integer mode;
    /**
     * 机器人的 x 坐标, 单位 m
     */
    private String x;
    /**
     * 机器人的 y坐标, 单位 m
     */
    private String y;
    /**
     * 机器人当前所在站点的 id（该判断比	较严格，机器人必须很靠近某一个站	点(<30cm， 这个距离可以通过参数	配置中的 CurrentPointDist 修改)，否	则为空字符，即不处于任何站点

     */
    private String currentStation;
    /**
     * 机器人上一个所在站点的 id

     */
    private String lastStation;
    /**
     * 机器人底盘是否静止（行走电机）
     *
     * 枚举 {@link TODO infra_boolean_string 对应的类}
     */
    private Boolean isStop;
    /**
     * 机器人是否被阻挡

     *
     * 枚举 {@link TODO infra_boolean_string 对应的类}
     */
    private Boolean blocked;
    /**
     * true 表示急停按钮处于激活状态(按	下), false 表示急停按钮处于非激活状	态(拔起)
     *
     * 枚举 {@link TODO infra_boolean_string 对应的类}
     */
    private Boolean emergency;
    /**
     * 0 = NONE, 1 = WAITING(目前不可能	出现该状态), 2 = RUNNING, 3 =	SUSPENDED, 4 = COMPLETED, 5 =	FAILED, 6 = CANCELED

     *
     * 枚举 {@link TODO wms_agv_status 对应的类}
     */
    private String taskStatus;
    /**
     * 自身库位id
     */
    private String locationId;

}