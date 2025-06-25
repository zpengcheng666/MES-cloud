package com.miyu.module.qms.dal.dataobject.inspectionitemtype;

import lombok.*;
import java.util.*;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.*;
import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;

/**
 * 检测项目分类 DO
 *
 * @author 芋道源码
 */
@TableName("qms_inspection_item_type")
@KeySequence("qms_inspection_item_type_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InspectionItemTypeDO extends BaseDO {

    public static final String PARENT_ID_ROOT = "0";

    /**
     * 主键
     */
    @TableId(type = IdType.ASSIGN_ID)
    private String id;
    /**
     * 父项目分类ID
     */
    private String parentId;
    /**
     * 检测项目分类名称
     */
    private String name;

}