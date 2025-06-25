package com.miyu.module.mcc.dal.dataobject.encodingattribute;

import lombok.*;
import java.util.*;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.*;
import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;

/**
 * 编码自定义属性 DO
 *
 * @author 上海弥彧
 */
@TableName("mcc_encoding_attribute")
@KeySequence("mcc_encoding_attribute_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EncodingAttributeDO extends BaseDO {

    /**
     * ID
     */
    @TableId(type = IdType.ASSIGN_ID)
    private String id;
    /**
     * 属性名称
     */
    private String name;
    /**
     * 属性值
     */
    private String code;

}