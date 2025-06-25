package com.miyu.module.tms.dal.dataobject.toolinfo;

import lombok.*;
import java.util.*;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import java.math.BigDecimal;
import java.math.BigDecimal;
import java.math.BigDecimal;
import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.*;
import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;

/**
 * 刀具动平衡 DO
 *
 * @author QianJy
 */
@TableName("tms_tool_balance")
@KeySequence("tms_tool_balance_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ToolBalanceDO extends BaseDO {

    /**
     * ID
     */
    @TableId(type = IdType.ASSIGN_ID)
    private String id;
    /**
     * 平衡质量等级
     */
    private BigDecimal balancingQuality;
    /**
     * 操作速度
     */
    private Integer serviceSpeed;
    /**
     * 质量单位[KG]
     */
    private BigDecimal weight;
    /**
     * 标准值[gmm]
     */
    private BigDecimal staticUnbalance;
    /**
     * 动平衡转速
     */
    private Integer rpm;
    /**
     * 质量等级结果
     */
    private Integer balancingQualityReality;
    /**
     * 允许机床转速
     */
    private Integer maxSpeed;
    /**
     * 结果[gmm]
     */
    private BigDecimal gmmResult;
    /**
     * 成品刀具id
     */
    private String toolInfoId;

}