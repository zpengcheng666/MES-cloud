package com.miyu.module.mcc.dal.dataobject.encodingrule;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.*;
import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;

/**
 * 编码规则配置 DO
 *
 * @author 上海弥彧
 */
@TableName("mcc_encoding_rule")
@KeySequence("mcc_encoding_rule_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EncodingRuleDO extends BaseDO {

    /**
     * ID
     */
    @TableId(type = IdType.ASSIGN_ID)
    private String id;
    /**
     * 名称
     */
    private String name;
    /**
     * 编码分类
     */
    private String classificationId;
    /**
     * 启用状态  1启用 0未启用
     *
     * 枚举 {@link TODO mcc_enable_status 对应的类}
     */
    private Integer status;
    /**
     * 总位数
     */
    private Integer totalBitNumber;


    /***
     * 所属类别（树形结构）
     */
    private String materialTypeId;


    /***
     * 类型 1普通码 2追加码
     */
    private Integer encodingRuleType;

    /***
     * 所属分类
     */
    @TableField(exist = false)
    private String classificationName;
    @TableField(exist = false)
    private String classificationCode;
    /***
     * 所属类别（树形结构）
     */
    @TableField(exist = false)
    private String materialTypeCode;
    @TableField(exist = false)
    private String materialTypeName;

    /***
     * 自动释放流水号
     */
    private Integer autoRelease;

    /**
     * 根据规则生成的码
     */
    @TableField(exist = false)
    private String generatorCode;
}