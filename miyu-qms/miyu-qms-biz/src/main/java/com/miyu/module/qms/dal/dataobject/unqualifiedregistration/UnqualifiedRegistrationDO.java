package com.miyu.module.qms.dal.dataobject.unqualifiedregistration;

import lombok.*;
import com.baomidou.mybatisplus.annotation.*;
import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;

/**
 * 不合格品登记 DO
 *
 * @author Zhangyunfei
 */
@TableName("qms_unqualified_registration")
@KeySequence("qms_unqualified_registration_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UnqualifiedRegistrationDO extends BaseDO {

    /**
     * 主键
     */
    @TableId(type = IdType.ASSIGN_ID)
    private String id;

    /**
     * 缺陷代码
     */
    private String defectiveCode;

    /**
     * 检验单方案任务ID
     */
    private String inspectionSheetSchemeId;

    /**
     * 不合格产品ID
     */
    private String unqualifiedMaterialId;

    @TableField(exist = false)
    private String name;
    @TableField(exist = false)
    private String materialConfigId;
    @TableField(exist = false)
    private String batchNumber;
    @TableField(exist = false)
    private Integer schemeType;
}