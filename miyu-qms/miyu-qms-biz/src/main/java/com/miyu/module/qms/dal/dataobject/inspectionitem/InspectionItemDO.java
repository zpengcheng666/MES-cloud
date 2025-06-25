package com.miyu.module.qms.dal.dataobject.inspectionitem;

import lombok.*;
import java.util.*;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.*;
import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;

/**
 * 检测项目 DO
 *
 * @author 芋道源码
 */
@TableName("qms_inspection_item")
@KeySequence("qms_inspection_item_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InspectionItemDO extends BaseDO {

    /**
     * 主键
     */
    @TableId(type = IdType.ASSIGN_ID)
    private String id;
    /**
     * 检测项目名称
     */
    private String itemName;
    /**
     * 检测项目编号
     */
    private String itemNo;
    /**
     * 检测项目描述
     */
    private String content;
    /**
     * 检测方式  1定性 2定量
     */
    private Integer inspectionType;
    /**
     * 检测工具  目测 皮尺测量 
     */
    private String inspectionToolId;
    /**
     * 检测项目分类ID
     */
    private String itemTypeId;

    @TableField(exist = false)
    private String itemTypeName;

    @TableField(exist = false)
    private String inspectionToolName;

    @TableField(exist = false)
    private String materialConfigId;

}