package com.miyu.module.qms.dal.dataobject.inspectionsheetsamplingrule;

import lombok.*;
import com.baomidou.mybatisplus.annotation.*;
import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;

/**
 * 检验单抽样规则（检验抽样方案）关系 DO
 *
 * @author Zhangyunfei
 */
@TableName("qms_inspection_sheet_sampling_rule")
@KeySequence("qms_inspection_sheet_sampling_rule_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InspectionSheetSamplingRuleDO extends BaseDO {

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
     * 抽样方案ID
     */
    private String samplingRuleConfigId;
    /**
     * 抽样标准ID
     */
    private String samplingStandardId;

}