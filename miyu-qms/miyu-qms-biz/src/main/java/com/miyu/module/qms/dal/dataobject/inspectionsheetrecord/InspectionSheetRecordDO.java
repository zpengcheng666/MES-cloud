package com.miyu.module.qms.dal.dataobject.inspectionsheetrecord;

import lombok.*;

import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.*;
import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;

/**
 * 检验记录 DO
 *
 * @author Zhangyunfei
 */
@TableName("qms_inspection_sheet_record")
@KeySequence("qms_inspection_sheet_record_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InspectionSheetRecordDO extends BaseDO {

    /**
     * 主键
     */
    @TableId(type = IdType.ASSIGN_ID)
    private String id;

    /**
     * 检测任务ID
     */
    private String inspectionSheetSchemeId;

    /**
     * 检测项目ID
     */
    private String inspectionSchemeItemId;
 
    /**
     * 测量结果
     */
    private String content;
    /**
     * 是否合格
     */
    private Integer inspectionResult;

    /**
     * 互检测量结果
     */
    private String mutualContent;
    /**
     * 互检是否合格
     */
    private Integer mutualInspectionResult;

    /**
     * 专检测量结果
     */
    private String specContent;

    /**
     * 专检是否合格
     */
    private Integer specInspectionResult;

    /**
     * 检验单物料表ID
     */
    private String schemeMaterialId;

    /**
     * 检测项名称
     */
    @TableField(exist = false)
    private String inspectionSchemeItemName;

    /**
     * 检测项配置名称
     */
    @TableField(exist = false)
    private String inspectionItemTypeName;

    /**
     * 判断方式  文本   是否  数字  条件判断
     *
     */
    @TableField(exist = false)
    private Integer referenceType;

    /**
     * 上限值
     */
    @TableField(exist = false)
    private BigDecimal schemeMaxValue;

    /**
     * 下限值
     */
    @TableField(exist = false)
    private BigDecimal schemeMinValue;

    /**
     * 技术要求
     */
    @TableField(exist = false)
    private String schemeContent;

    /**
     * 判断
     */
    @TableField(exist = false)
    private Integer judgement;

    @TableField(exist = false)
    private String batchNumber;

    @TableField(exist = false)
    private String materialConfigId;

}