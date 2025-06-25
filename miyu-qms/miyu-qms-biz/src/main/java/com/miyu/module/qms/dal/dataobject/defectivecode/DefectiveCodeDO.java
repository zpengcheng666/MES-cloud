package com.miyu.module.qms.dal.dataobject.defectivecode;

import lombok.*;
import com.baomidou.mybatisplus.annotation.*;
import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;

/**
 * 缺陷代码 DO
 *
 * @author Zhangyunfei
 */
@TableName("qms_defective_code")
@KeySequence("qms_defective_code_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DefectiveCodeDO extends BaseDO {

    /**
     * 主键
     */
    @TableId(type = IdType.ASSIGN_ID)
    private String id;
    /**
     * 缺陷代码名称
     */
    private String name;
    /**
     * 缺陷代码
     */
    private String code;

}