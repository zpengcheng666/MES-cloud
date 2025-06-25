package com.miyu.cloud.macs.dal.dataobject.collectorStrategy;

import lombok.*;
import java.util.*;
import com.baomidou.mybatisplus.annotation.*;
import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;

/**
 * 设备策略 DO
 *
 * @author miyu
 */
@TableName("macs_collector_strategy")
@KeySequence("macs_collector_strategy_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CollectorStrategyDO {

    /**
     * 主键id
     */
    @TableId(type = IdType.INPUT)
    private String id;
    /**
     * 设备id
     */
    private String collectorId;
    /**
     * 策略id
     */
    private String strategyId;

}
