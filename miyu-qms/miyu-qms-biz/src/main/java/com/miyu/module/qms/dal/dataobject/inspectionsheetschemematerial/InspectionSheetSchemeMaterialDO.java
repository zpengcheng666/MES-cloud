package com.miyu.module.qms.dal.dataobject.inspectionsheetschemematerial;

import lombok.*;
import com.baomidou.mybatisplus.annotation.*;
import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;

import java.time.LocalDateTime;

/**
 * 检验单产品 DO
 *
 * @author Zhangyunfei
 */
@TableName("qms_inspection_sheet_scheme_material")
@KeySequence("qms_inspection_sheet_scheme_material_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InspectionSheetSchemeMaterialDO extends BaseDO {

    /**
     * 主键
     */
    @TableId(type = IdType.ASSIGN_ID)
    private String id;
    /**
     * 测量结果
     */
    private String content;
    /**
     * 是否合格
     */
    private Integer inspectionResult;
    /**
     * 物料ID
     */
    private String materialId;
    /**
     * 物料类型ID
     */
    private String materialConfigId;
    /**
     * 物料条码
     */
    private String barCode;
    /**
     * 物料批次号
     */
    private String batchNumber;


    /**
     * 物料条码
     */
    private String barCodeCheck;

    /**
     * 产品检验状态
     */
    private Integer status;

    /**
     * 互检是否合格
     */
    private Integer mutualInspectionResult;

    /**
     * 专检是否合格
     */
    private Integer specInspectionResult;

    /**
     * 自检状态
     */
    private Integer selfStatus;

    /**
     * 互检状态
     */
    private Integer mutualStatus;

    /**
     * 专检状态
     */
    private Integer specStatus;


    /**
     * 检验单任务表ID
     */
    private String inspectionSheetSchemeId;

    /**
     * 物料编号
     */
    @TableField(exist = false)
    private String materialNumber;

    /**
     * 物料类码
     */
    @TableField(exist = false)
    private String materialCode;

    /**
     * 物料名称
     */
    @TableField(exist = false)
    private String materialName;

    /**
     * 物料属性（成品、毛坯、辅助材料）
     */
    @TableField(exist = false)
    private Integer materialProperty;

    /**
     * 物料类型（零件、托盘、工装、夹具、刀具）
     */
    @TableField(exist = false)
    private String materialType;

    /**
     * 物料规格
     */
    @TableField(exist = false)
    private String materialSpecification;

    /**
     * 物料品牌
     */
    @TableField(exist = false)
    private String materialBrand;

    /**
     * 物料单位
     */
    @TableField(exist = false)
    private String materialUnit;

    /**
     * 分配的检验人员
     */
    @TableField(exist = false)
    private String assignmentId;
    /**
     * 分配日期
     */
    @TableField(exist = false)
    private LocalDateTime assignmentDate;

    /**
     * 质检状态
     */
    @TableField(exist = false)
    private String technologyId;

    /**
     * 质检状态
     */
    @TableField(exist = false)
    private String processId;

    /**
     * 检验单名称
     */
    @TableField(exist = false)
    private String sheetName;

    /**
     * 检验单号
     */
    @TableField(exist = false)
    private String sheetNo;

    /**
     * 缺陷代码
     */
    @TableField(exist = false)
    private String defectiveCode;

    /**
     * 缺陷等级
     */
    @TableField(exist = false)
    private Integer defectiveLevel;

    /**
     * 不合格品处理方式 字段放到不合格产品表中
     */
    @TableField(exist = false)
     private Integer handleMethod;

    /**
     * 生成单号
     */
    @TableField(exist = false)
    private String recordNumber;
}
