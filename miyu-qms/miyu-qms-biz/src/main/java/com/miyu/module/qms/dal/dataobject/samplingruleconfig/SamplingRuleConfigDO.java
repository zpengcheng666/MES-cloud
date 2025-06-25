package com.miyu.module.qms.dal.dataobject.samplingruleconfig;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.util.*;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import java.math.BigDecimal;

import com.baomidou.mybatisplus.annotation.*;
import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;

/**
 * 抽样规则（检验抽样方案） DO
 *
 * @author 芋道源码
 */
@TableName("qms_sampling_rule_config")
@KeySequence("qms_sampling_rule_config_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SamplingRuleConfigDO extends BaseDO {

    /**
     * 主键
     */
    @TableId(type = IdType.ASSIGN_ID)
    private String id;
    /**
     * 样本量字码
     * <p>
     * 枚举 {@link TODO sample_size_code 对应的类}
     */
    private String sampleSizeCode;
    /**
     * 抽取样本数量
     */
    private Integer sampleSize;
    /**
     * 类型  1正常检查2加严检查3放宽检查
     * <p>
     * 枚举 {@link TODO sampling_rule_type 对应的类}
     */
    private Integer samplingRuleType;
    /**
     * 接收质量限（AQL）
     */
    private BigDecimal acceptanceQualityLimit;
    /**
     * 接收数（Ac）
     */
    private Integer acceptNum;
    /**
     * 拒收数（Re）
     */
    private Integer rejectionNum;

    /***
     * 抽样标准
     */
    private String samplingStandardId;

    @TableField(exist = false)
    private String samplingStandardName;

}