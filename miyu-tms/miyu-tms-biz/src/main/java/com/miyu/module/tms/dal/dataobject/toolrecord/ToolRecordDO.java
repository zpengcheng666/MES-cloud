package com.miyu.module.tms.dal.dataobject.toolrecord;

import lombok.*;
import java.util.*;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.*;
import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;

/**
 * 刀具使用记录 DO
 *
 * @author QianJy
 */
@TableName("tms_tool_record")
@KeySequence("tms_tool_record_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ToolRecordDO extends BaseDO {

    /**
     * ID
     */
    @TableId(type = IdType.ASSIGN_ID)
    private String id;
    /**
     * 成品刀具id
     */
    private String toolInfoId;
    /**
     * 起始时间
     */
    private LocalDateTime startTime;
    /**
     * 结束时间
     */
    private LocalDateTime endTime;
    /**
     * 表字段id（目标设备、目标位置、等）
     */
    private String field;
    /**
     * 类型（装配、测量、出库、配送、上机、使用、下机、回库、入库、拆卸）
     *
     * 枚举 {@link TODO tool_record_type 对应的类}
     */
    private Integer type;


    @TableField(exist = false)
    private String mainStockId;
    @TableField(exist = false)
    private String barCode;
    @TableField(exist = false)
    private String materialNumber;
    @TableField(exist = false)
    private String materialName;
}