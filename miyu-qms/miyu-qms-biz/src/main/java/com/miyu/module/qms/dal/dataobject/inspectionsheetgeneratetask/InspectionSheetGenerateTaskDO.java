package com.miyu.module.qms.dal.dataobject.inspectionsheetgeneratetask;
import lombok.*;
import com.baomidou.mybatisplus.annotation.*;
import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;

/**
 * 检验单 DO
 *
 * @author Zhangyunfei
 */
@TableName("qms_inspection_sheet_generate_task")
@KeySequence("qms_inspection_sheet_generate_task_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InspectionSheetGenerateTaskDO extends BaseDO {

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
     * 来源单号
     */
    private String recordNumber;
    /**
     * 质检状态  0待派工1待检验2检验中3检验完成
     */
    private Integer status;
    /**
     * 检验单来源  	1采购收货	2外协收获	3外协原材料退货	4委托加工收货	5销售退货	6生产
     */
    private Integer sourceType;

    /**
     * 物料类型
     */
    private String materialConfigId;

    /**
     * 批次号
     */
    private String batchNumber;

    /**
     * 工序任务ID
     */
    private String  recordId;

    /**
     * 方案类型 1来料检验  2生产检验 3成品检验
     */
    private Integer schemeType;

    /**
     * 工艺ID
     */
    private String technologyId;

    /**
     * 工序ID
     */
    private String processId;

    /**
     * 数量
     */
    @TableField(exist = false)
    private Integer quantity;
}
