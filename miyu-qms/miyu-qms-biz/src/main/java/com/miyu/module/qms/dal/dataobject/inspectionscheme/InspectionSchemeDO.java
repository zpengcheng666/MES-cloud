package com.miyu.module.qms.dal.dataobject.inspectionscheme;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.math.BigDecimal;
import java.util.*;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.*;
import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;

/**
 * 检验方案 DO
 *
 * @author 芋道源码
 */
@TableName("qms_inspection_scheme")
@KeySequence("qms_inspection_scheme_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InspectionSchemeDO extends BaseDO {

    /**
     * 主键
     */
    @TableId(type = IdType.ASSIGN_ID)
    private String id;
    /**
     * 方案名称
     */
    private String schemeName;
    /**
     * 方案编号
     */
    private String schemeNo;
    /**
     * 方案类型 来料检验  过程检验 完工检验
     *
     *
     */
    private Integer schemeType;
    /**
     * 物料类型ID
     */
    private String materialConfigId;
    /**
     * 检验级别
     */
    private Integer inspectionLevel;
    /**
     * 是否生效
     */
    private Integer isEffective;

    /**
     * 物料编号
     */
    private String materialNumber;

    /**
     * 物料名称
     */
    private String materialName;

    /**
     * 工艺ID
     */
    private String technologyId;
    /**
     * 工序ID
     */
    private String processId;
    /***
     * 抽样标准
     */
    private String samplingStandardId;

    /***
     * 抽样准则
     */
    private Integer samplingLimitType;
    /**
     * 接收质量限（AQL）
     */
    private BigDecimal acceptanceQualityLimit;

    @TableField(exist = false)
    private String samplingStandardName;

    /**
     * 检验类型1抽检2全检
     */
    private Integer inspectionSheetType;

    /**
     * 检验水平类型
     */
    private Integer inspectionLevelType;
    /**
     * 类型  1正常检查2加严检查3放宽检查
     */
    private Integer samplingRuleType;

    /**
     * 是否专检
     */
    private String isInspect;
}
