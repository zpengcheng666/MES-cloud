package com.miyu.module.dc.dal.dataobject.collectattributes;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.math.BigDecimal;
import java.util.*;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.*;
import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;
import org.springframework.format.annotation.DateTimeFormat;

import static cn.iocoder.yudao.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

/**
 * 采集属性 DO
 *
 * @author 芋道源码
 */
@TableName("dc_collect_attributes")
@KeySequence("dc_collect_attributes_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CollectAttributesDO extends BaseDO {

    /**
     * 采集属性主键
     */
    @TableId(type = IdType.ASSIGN_ID)
    private String id;

    /**
     * 采集类型名称
     */
    private String productTypeId;

    /**
     * 采集属性名称
     */
    private String collectAttributesName;

    /**
     * 采集属性值
     */
    private String collectAttributesValue;

    /**
     * 标准上限值
     */
    private BigDecimal collectAttributesUpper;

    /**
     * 单位
     */
    private String unit;

    /**
     * 标准下限值
     */
    private BigDecimal collectAttributesFloor;

    /**
     * 采集参数类型
     */
    private Integer collectAttributesIlk;

    /**
     * 标准值(字符串)
     */
    private String collectAttributesNorm;

    /**
     * 标准枚举值(枚举)
     */
    private String collectAttributesEnum;

    @TableField(exist = false)
    private String collectAttributesTypeName;

}