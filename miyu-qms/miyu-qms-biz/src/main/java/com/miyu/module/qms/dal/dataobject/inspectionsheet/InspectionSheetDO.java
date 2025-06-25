package com.miyu.module.qms.dal.dataobject.inspectionsheet;

import lombok.*;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.*;
import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;

/**
 * 检验单 DO
 *
 * @author Zhangyunfei
 */
@TableName("qms_inspection_sheet")
@KeySequence("qms_inspection_sheet_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InspectionSheetDO extends BaseDO {

    /**
     * 主键
     */
    @TableId(type = IdType.ASSIGN_ID)
    private String id;
    /**
     * 检验单名称
     */
    private String sheetName;
    /**
     * 检验单号
     */
    private String sheetNo;

    /**
     * 源单号
     */
    private String recordNumber;

    /**
     * 任务ID
     */
    private String recordId;


    /**
     * 质检状态  0待派工1待检验2检验中3检验完成
     */
    private Integer status;
    /**
     * 负责人
     */
    private String header;
    /**
     * 开始时间
     */
    private LocalDateTime beginTime;
    /**
     * 结束时间
     */
    private LocalDateTime endTime;

    /**
     * 检验单来源
     */
    private Integer sourceType;

    //查询总检验单新增显示字段
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
    private Integer materialType;

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
     * 方案类型 来料检验  生产检验
     */
    @TableField(exist = false)
    private Integer schemeType;

    /**
     * 检验类型1抽检2全检
     */
    @TableField(exist = false)
    private Integer inspectionSheetType;

    /**
     * 分配类型 1人员 2班组
     */
    @TableField(exist = false)
    private Integer assignmentType;

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
     * 物料类型ID
     */
    @TableField(exist = false)
    private String materialConfigId;

    /**
     * 产品编号
     */
    @TableField(exist = false)
    private String materialId;


    /**
     * 物料条码
     */
    @TableField(exist = false)
    private String barCode;

    /**
     * 合格数量
     */
    @TableField(exist = false)
    private Integer qualifiedQuantity;

    /**
     * 检测结果 1合格 2不合格
     */
    @TableField(exist = false)
    private Integer inspectionResult;

    /**
     * 检测数量
     */
    @TableField(exist = false)
    private Integer inspectionQuantity;


    /**
     * 实际检测数量
     */
    @TableField(exist = false)
    private Integer quantity;

    /**
     * 通过准则
     */
    @TableField(exist = false)
    private String passRule;

    /**
     * 测量结果
     */
    @TableField(exist = false)
    private String content;

    /**
     * 检验任务质检状态
     */
    @TableField(exist = false)
    private Integer schemeStatus;

    /**
     * 是否自检
     */
    @TableField(exist = false)
    private Integer selfInspection;

    /**
     * 自检状态
     */
    @TableField(exist = false)
    private Integer selfAssignmentStatus;


    /**
     * 工艺ID
     */
    @TableField(exist = false)
    private String technologyId;

    /**
     * 工序ID
     */
    @TableField(exist = false)
    private String processId;
}
