package com.miyu.module.qms.dal.dataobject.inspectionsheetgeneratetaskdetail;

import lombok.*;
import com.baomidou.mybatisplus.annotation.*;
import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;

/**
 * 检验单生成任务明细 DO
 *
 * @author Zhangyunfei
 */
@TableName("qms_inspection_sheet_generate_task_detail")
@KeySequence("qms_inspection_sheet_generate_task_detail_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InspectionSheetGenerateTaskDetailDO extends BaseDO {

    /**
     * 主键
     */
    @TableId(type = IdType.ASSIGN_ID)
    private String id;
    /**
     * 检验单任务ID
     */
    private String taskId;
    /**
     * 物料ID
     */
    private String materialId;

    /**
     * 物料条码
     */
    private String barCode;
    /**
     * 物料批次号
     */
    private String batchNumber;

}
