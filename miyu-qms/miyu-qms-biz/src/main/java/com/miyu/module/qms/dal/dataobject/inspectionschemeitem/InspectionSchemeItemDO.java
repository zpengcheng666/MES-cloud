package com.miyu.module.qms.dal.dataobject.inspectionschemeitem;

import lombok.*;

import java.math.BigDecimal;
import java.util.*;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.*;
import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;

/**
 * 检验方案检测项目详情 DO
 *
 * @author 芋道源码
 */
@TableName("qms_inspection_scheme_item")
@KeySequence("qms_inspection_scheme_item_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InspectionSchemeItemDO extends BaseDO {

    /**
     * 主键
     */
    @TableId(type = IdType.ASSIGN_ID)
    private String id;
    /**
     * 方案ID
     */
    private String inspectionSchemeId;
    /**
     * 检测项目ID
     */
    private String inspectionItemId;
    /**
     * 检测顺序
     */
    private Integer number;

    /**
     * 判断方式  文本   是否  数字  条件判断
     *
     * 枚举 {@link TODO reference_type 对应的类}
     */
    private Integer referenceType;
    /**
     * 上限值
     */
    private BigDecimal maxValue;
    /**
     * 下限值
     */
    private BigDecimal minValue;
    /**
     * 技术要求
     */
    private String content;
    /**
     * 判断
     */
    private Integer judgement;

    /**
     * 接收质量限（AQL）
     */
    private BigDecimal acceptanceQualityLimit;
    /**
     * 方案名称
     */
    @TableField(exist = false)
    private String inspectionSchemeName;
    /***
     * 检测项名称
     */
    @TableField(exist = false)
    private String inspectionItemName;


    @TableField(exist = false)
    private String itemTypeId;

    @TableField(exist = false)
    private String itemTypeName;

    @TableField(exist = false)
    private String inspectionToolName;
}