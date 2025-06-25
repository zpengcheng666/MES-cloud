package com.miyu.module.mcc.dal.dataobject.encodingclassification;

import lombok.*;
import java.util.*;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.*;
import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;

/**
 * 编码分类 DO
 *
 * @author 上海弥彧
 */
@TableName("mcc_encoding_classification")
@KeySequence("mcc_encoding_classification_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EncodingClassificationDO extends BaseDO {

    /**
     * ID
     */
    @TableId(type = IdType.ASSIGN_ID)
    private String id;
    /**
     * 编码分类CODE
     */
    private String code;
    /**
     * 编码分类名称
     */
    private String name;
    /**
     * 分类所属服务
     */
    private String service;
    /**
     * 分类查看编码使用地址
     */
    private String path;

}