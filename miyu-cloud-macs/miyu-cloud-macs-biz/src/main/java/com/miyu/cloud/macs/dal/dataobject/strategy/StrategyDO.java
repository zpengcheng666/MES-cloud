package com.miyu.cloud.macs.dal.dataobject.strategy;

import lombok.*;
import java.util.*;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.*;
import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;
import org.apache.ibatis.type.JdbcType;

/**
 * 策略 DO
 *
 * @author 芋道源码
 */
@TableName("macs_strategy")
@KeySequence("macs_strategy_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StrategyDO {

    /**
     * 主键id
     */
    @TableId(type = IdType.ASSIGN_ID)
    private String id;
    /**
     * 编码
     */
    private String code;
    /**
     * 名称
     */
    private String name;
    /**
     * 描述
     */
    private String description;
    /**
     * 状态（1启用 0不启用）
     */
    private Integer status;
    /**
     * 创建人
     */
    @TableField(fill = FieldFill.INSERT, jdbcType = JdbcType.VARCHAR)
    private String createBy;
    /**
     * 更新人
     */
    @TableField(fill = FieldFill.INSERT_UPDATE, jdbcType = JdbcType.VARCHAR)
    private String updateBy;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;

    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

}
