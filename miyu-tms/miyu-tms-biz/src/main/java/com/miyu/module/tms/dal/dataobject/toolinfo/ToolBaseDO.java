package com.miyu.module.tms.dal.dataobject.toolinfo;

import lombok.*;
import java.util.*;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import java.math.BigDecimal;
import java.math.BigDecimal;
import java.math.BigDecimal;
import java.math.BigDecimal;
import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.*;
import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;

/**
 * 对刀数据 DO
 *
 * @author QianJy
 */
@TableName("tms_tool_base")
@KeySequence("tms_tool_base_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ToolBaseDO extends BaseDO {

    /**
     * ID
     */
    @TableId(type = IdType.ASSIGN_ID)
    private String id;
    /**
     * 刀具直径
     */
    private BigDecimal diameter;
    /**
     * 刀具总长
     */
    private BigDecimal totalLength;
    /**
     * 刀具r角
     */
    private BigDecimal rAngle;
    /**
     * 刀具额定寿命
     */
    private BigDecimal ratedLife;
    /**
     * 剩余寿命
     */
    private BigDecimal remainLife;
    /**
     * 成品刀具id
     */
    private String toolInfoId;

}