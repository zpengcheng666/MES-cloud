package com.miyu.module.mcc.dal.dataobject.coderecord;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.*;
import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;

/**
 * 编码记录 DO
 *
 * @author 上海弥彧
 */
@TableName("mcc_code_record")
@KeySequence("mcc_code_record_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CodeRecordDO extends BaseDO {

    /**
     * ID
     */
    @TableId(type = IdType.ASSIGN_ID)
    private String id;
    /**
     * 编码
     */
    private String code;
    /**
     * 名称
     */
    private String name;
    /**
     * 父类型id
     */
    private String parentId;
    /**
     * 状态  1 预生成 2 已使用  3释放
     *
     * 枚举 {@link TODO mcc_record_status 对应的类}
     */
    private Integer status;
    /**
     * 编码规则ID
     */
    private String encodingRuleId;
    /**
     * 编码分类ID
     */
    private String classificationId;
    /***
     * 请求入参
     */
    private String params;

    @TableField(exist = false)
    private String encodingRuleName;
    @TableField(exist = false)
    private String classificationName;
    @TableField(exist = false)
    private String classificationCode;

}