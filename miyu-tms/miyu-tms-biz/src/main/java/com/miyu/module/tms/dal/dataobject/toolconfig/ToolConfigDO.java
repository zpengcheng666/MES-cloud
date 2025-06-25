package com.miyu.module.tms.dal.dataobject.toolconfig;
import lombok.*;
import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.*;
import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;

/**
 * 刀具类型 DO
 *
 * @author QianJy
 */
@TableName("tms_tool_config")
@KeySequence("tms_tool_config_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ToolConfigDO extends BaseDO {

    /**
     * ID
     */
    @TableId(type = IdType.ASSIGN_ID)
    private String id;
    /**
     * 物料类型id
     */
    private String materialConfigId;

    /**
     * 名称
     */
    private String materialNumber;

    /**
     * 名称
     */
    private String toolName;
    /**
     * 型号
     */
    private String toolModel;
    /**
     * 重量
     */
    private Double toolWeight;
    /**
     * 材质
     */
    private String toolTexture;
    /**
     * 涂层
     */
    private String toolCoating;
    /**
     * 额定寿命
     */
    private BigDecimal ratedLife;

    /**
     * 最高转速(mm)
     */
    private Integer maxSpeed;
    /**
     * 总长上限(mm)
     */
    private BigDecimal lengthUpper;
    /**
     * 总长下限(mm)
     */
    private BigDecimal lengthFloor;
    /**
     * 玄长上限(mm)
     */
    private BigDecimal hangingLengthUpper;
    /**
     * 玄长下限(mm)
     */
    private BigDecimal hangingLengthFloor;
    /**
     * 刃径上偏差(mm)
     */
    private BigDecimal bladeUpperDeviation;
    /**
     * 刃径下偏差(mm)
     */
    private BigDecimal bladeFloorDeviation;
    /**
     * 底R上偏差(mm)
     */
    private BigDecimal rUpperDeviation;
    /**
     * 底R下偏差(mm)
     */
    private BigDecimal rFloorDeviation;
    /**
     * 刀组状态0停用1正常
     */
    private Integer status;

    /**
     * 刀具类型
     */
    private String toolType;

    /**
     * 物料类别ID
     */
    private String materialTypeId;

    /**
     * 刀具类码
     */
    private String materialTypeCode;

    /**
     * 物料类别名称
     */
    private String materialTypeName;

    /**
     * 规格
     */
    private String spec;

}
