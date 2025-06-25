package com.miyu.module.qms.dal.dataobject.samplingrule;

import lombok.*;
import java.util.*;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.*;
import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;

/**
 * 抽样规则 DO
 *
 * @author 芋道源码
 */
@TableName("qms_sampling_rule")
@KeySequence("qms_sampling_rule_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SamplingRuleDO extends BaseDO {

    /**
     * 主键
     */
    @TableId(type = IdType.ASSIGN_ID)
    private String id;
    /**
     * 抽样标准ID
     */
    private String samplingStandardId;
    /**
     * 样本数字码
     *
     * 枚举 {@link TODO sample_size_code 对应的类}
     */
    private String sampleSizeCode;
    /**
     * 批量下限值（N）
     */
    private Integer minValue;
    /**
     * 批量上限值（N）
     */
    private Integer maxValue;
    /**
     * 检验水平类型
     *
     * 枚举 {@link TODO inspection_level_type 对应的类}
     */
    private Integer inspectionLevelType;

}