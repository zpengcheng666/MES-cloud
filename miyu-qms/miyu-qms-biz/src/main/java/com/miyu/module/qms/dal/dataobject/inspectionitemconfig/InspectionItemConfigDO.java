package com.miyu.module.qms.dal.dataobject.inspectionitemconfig;

import lombok.*;
import java.util.*;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.*;
import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;

/**
 * 检测项配置表（检测内容名称） DO
 *
 * @author 芋道源码
 */
@TableName("qms_inspection_item_config")
@KeySequence("qms_inspection_item_config_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InspectionItemConfigDO extends BaseDO {

    /**
     * 主键
     */
    @TableId(type = IdType.ASSIGN_ID)
    private String id;
    /**
     * 检测项配置表名称
     */
    private String name;
    /**
     * 编号
     */
    private String no;

}