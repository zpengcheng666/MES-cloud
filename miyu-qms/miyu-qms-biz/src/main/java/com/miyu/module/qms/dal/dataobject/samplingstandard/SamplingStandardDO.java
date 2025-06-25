package com.miyu.module.qms.dal.dataobject.samplingstandard;

import lombok.*;
import java.util.*;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.*;
import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;

/**
 * 抽样标准 DO
 *
 * @author Zhangyunfei
 */
@TableName("qms_sampling_standard")
@KeySequence("qms_sampling_standard_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SamplingStandardDO extends BaseDO {

    public static final String PARENT_ID_ROOT = "0";
    /**
     * 主键
     */
    @TableId(type = IdType.ASSIGN_ID)
    private String id;
    /**
     * 父抽样标准ID
     */
    private String parentId;
    /**
     * 抽样标准名称
     */
    private String name;

}