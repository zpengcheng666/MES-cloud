package com.miyu.module.qms.dal.dataobject.inspectionsheetscheme;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.baomidou.mybatisplus.annotation.*;
import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;

/**
 * 检验单方案任务计划 DO
 *
 * @author Zhangyunfei
 */
@TableName("qms_inspection_sheet_scheme")
@KeySequence("qms_inspection_sheet_scheme_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InspectionSheetSchemeDO extends BaseDO {

    /**
     * 主键
     */
    @TableId(type = IdType.ASSIGN_ID)
    private String id;
    /**
     * 检验单Id
     */
    private String inspectionSheetId;
    /**
     * 分配类型 1人员 2班组
     */
    private Integer assignmentType;
    /**
     * 分配的检验人员
     */
    private String assignmentId;

    /**
     * 分配的班组ID
     */
    private String assignmentTeamId;

    /**
     * 分配日期
     */
    private LocalDateTime assignmentDate;
    /**
     * 质检状态  0待派工1待检验2检验中3检验完成
     */
    private Integer status;

    /**
     * 互检人员
     */
    private String mutualAssignmentId;

    /**
     * 是否专检
     */
    private String  isSpecInspect;

    /**
     * 专检人员
     */
    private String  specAssignmentId;

    /**
     * 方案类型 来料检验  生产检验
     */
    private Integer schemeType;
    /**
     * 检验类型1抽检2全检
     */
    private Integer inspectionSheetType;
    /**
     * 是否首检
     */
    private Integer needFirstInspection;
    /**
     * 物料类型ID
     */
    private String materialConfigId;
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
    /**
     * 检验方案ID
     */
    private String inspectionSchemeId;
    /**
     * 通过准则
     */
    private String passRule;
    /**
     * 计划检验时间
     */
    private LocalDateTime planTime;
    /**
     * 实际开始时间
     */
    private LocalDateTime beginTime;
    /**
     * 实际结束时间
     */
    private LocalDateTime endTime;
    /**
     * 检测数量
     */
    private Integer inspectionQuantity;
    /**
     * 合格数量
     */
    private Integer qualifiedQuantity;
    /**
     * 检测结果 1合格 2不合格
     */
    private Integer inspectionResult;

    /**
     * 实际检测数量
     */
    private Integer quantity;

    /**
     * 检验水平类型
     */
    private Integer inspectionLevelType;
    /**
     * 类型  1正常检查2加严检查3放宽检查
     */
    private Integer samplingRuleType;

    /**
     * 物料批次号
     */
    private String batchNumber;

    /**
     * 是否自检
     */
    private Integer selfInspection;

    /**
     * 自检分配类型 1人员 2班组
     */
    private Integer selfAssignmentType;
    /**
     * 自检分配的检验人员
     */
    private String selfAssignmentId;
    /**
     * 自检分配日期
     */
    private LocalDateTime selfAssignmentDate;
    /**
     * 自检状态  0待分配 1已分配
     */
    private Integer selfAssignmentStatus;

    /**
     * 工作流编号
     */
    private String processInstanceId;

    /**
     * 审批状态
     */
    private Integer processStatus;

    /**
     * 接收质量限（AQL）
     */
    @TableField(exist = false)
    private BigDecimal acceptanceQualityLimit;

    @TableField(exist = false)
    private Integer toInspectionQuantity;

    @TableField(exist = false)
    private Integer inspectedQuantity;

    @TableField(exist = false)
    private Integer unqualifiedQuantity;

    @TableField(exist = false)
    private String recordNumber;

    @TableField(exist = false)
    private String sheetNo;

    @TableField(exist = false)
    private String sheetName;

    @TableField(exist = false)
    private String barCode;

    @TableField(exist = false)
    private String barCodes;

    /**
     * 检验单来源
     */
    @TableField(exist = false)
    private Integer sourceType;
}
