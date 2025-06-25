package com.miyu.module.tms.dal.dataobject.toolinfo;

import lombok.*;
import java.util.*;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.*;
import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;

/**
 * 刀具装配记录 DO
 *
 * @author QianJy
 */
@TableName("tms_assemble_record")
@KeySequence("tms_assemble_record_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AssembleRecordDO extends BaseDO {

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
     * 物料库存id（配刀库存）
     */
    private String materialStockId;
    /**
     * 刀位
     */
    private Integer site;
    /**
     * 数量
     */
    private Integer count;
    /**
     * 类型（装、卸、当前装、作废）
     */
    private Integer type;
    /**
     * 操作者(预留)
     */
    private String operator;

}