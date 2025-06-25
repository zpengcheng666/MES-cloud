package com.miyu.module.mcc.dal.dataobject.encodingruledetail;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.*;
import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;

/**
 * 编码规则配置详情 DO
 *
 * @author 上海弥彧
 */
@TableName("mcc_encoding_rule_detail")
@KeySequence("mcc_encoding_rule_detail_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EncodingRuleDetailDO extends BaseDO {

    /**
     * ID
     */
    @TableId(type = IdType.ASSIGN_ID)
    private String id;
    /**
     * 类型1机构 2编码分类 3类别4源码5连接符6数字流水号7字母流水号8年份9月份10日期
     *
     * 枚举 {@link TODO encoding_type 对应的类}
     */
    private Integer type;
    /**
     * 编码规则表ID
     */
    private String encodingRuleId;
    /**
     * 排序
     */
    private Integer sort;
    /**
     * 位数
     */
    private Integer bitNumber;
    /**
     * 描述名
     */
    private String name;
    /**
     * 默认值
     */
    private String defalutValue;
    /**
     * 规则 1固定  2自定义 3自生成
     *
     * 枚举 {@link TODO rule_type 对应的类}
     */
    private Integer ruleType;
    /**
     * 编码属性   （当自定义的时候可以选择属性方便传值）
     *
     * 枚举 {@link TODO encoding_attribute  对应的类}
     */
    private String encodingAttribute;
    /**
     * 来源规则Id
     */
    private String sourceRuleId;


    private String materialTypeId;
}