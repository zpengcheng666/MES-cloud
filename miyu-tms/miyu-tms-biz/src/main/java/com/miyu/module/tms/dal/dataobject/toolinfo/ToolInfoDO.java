package com.miyu.module.tms.dal.dataobject.toolinfo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.math.BigDecimal;
import java.util.*;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.*;
import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;

/**
 * 刀组信息 DO
 *
 * @author QianJy
 */
@TableName("tms_tool_info")
@KeySequence("tms_tool_info_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ToolInfoDO extends BaseDO {

    /**
     * ID
     */
    @TableId(type = IdType.ASSIGN_ID)
    private String id;
    /**
     * 物料id
     */
    private String materialStockId;
    /**
     * 物料类型id（冗余）
     */
    private String materialConfigId;
    /**
     * 状态(1启用、2装配中)
     */
    private Integer status;
    /**
     * 刀具装配任务id
     */
    private String assembleTaskId;


    // 成品刀具条码
    @TableField(exist = false)
    private String barCode;
    @Schema(description = "物料所在跟库位")
    @TableField(exist = false)
    private String rootLocationCode;
    @Schema(description = "物料所在跟储位托盘")
    @TableField(exist = false)
    private String storageCode;



    //工单号
    @TableField(exist = false)
    private String orderNumber;
    //目标位置
    @TableField(exist = false)
    private String targetLocation;
    @TableField(exist = false)
    private String targetLocationCode;
    //配送截止时间
    @TableField(exist = false)
    private LocalDateTime distributionDeadline;
    //物料类型id
    @TableField(exist = false)
    private String materialNumber;
    @TableField(exist = false)
    private String materialName;
    //最短加工时长
    @TableField(exist = false)
    private Integer minimumTime;


    // ===================对刀数据
    //刀具直径
    @TableField(exist = false)
    private BigDecimal diameter;
    //刀具总长
    @TableField(exist = false)
    private BigDecimal totalLength;
    //刀具r角
    @TableField(exist = false)
    private BigDecimal rAngle;
    //刀具额定寿命
    @TableField(exist = false)
    private BigDecimal ratedLife;
    //剩余寿命
    @TableField(exist = false)
    private BigDecimal remainLife;

    // ===================动平衡数据
    //平衡质量等级
    @TableField(exist = false)
    private BigDecimal balancingQuality;
    //操作速度
    @TableField(exist = false)
    private Integer serviceSpeed;
    //质量单位[KG]
    @TableField(exist = false)
    private BigDecimal weight;
    //标准值[gmm]
    @TableField(exist = false)
    private BigDecimal staticUnbalance;
    //动平衡转速
    @TableField(exist = false)
    private Integer rpm;
    //质量等级结果
    @TableField(exist = false)
    private Integer balancingQualityReality;
    //允许机床转速
    @TableField(exist = false)
    private Integer maxSpeed;
    //结果[gmm]
    @TableField(exist = false)
    private BigDecimal gmmResult;



    /**
     * 名称
     */
    @TableField(exist = false)
    private String toolName;
    /**
     * 型号
     */
    @TableField(exist = false)
    private String toolModel;
    /**
     * 重量
     */
    @TableField(exist = false)
    private Double toolWeight;
    /**
     * 材质
     */
    @TableField(exist = false)
    private String toolTexture;
    /**
     * 涂层
     */
    @TableField(exist = false)
    private String toolCoating;

    /**
     * 总长上限(mm)
     */
    @TableField(exist = false)
    private BigDecimal lengthUpper;
    /**
     * 总长下限(mm)
     */
    @TableField(exist = false)
    private BigDecimal lengthFloor;
    /**
     * 玄长上限(mm)
     */
    @TableField(exist = false)
    private BigDecimal hangingLengthUpper;
    /**
     * 玄长下限(mm)
     */
    @TableField(exist = false)
    private BigDecimal hangingLengthFloor;
    /**
     * 刃径上偏差(mm)
     */
    @TableField(exist = false)
    private BigDecimal bladeUpperDeviation;
    /**
     * 刃径下偏差(mm)
     */
    @TableField(exist = false)
    private BigDecimal bladeFloorDeviation;
    /**
     * 底R上偏差(mm)
     */
    @TableField(exist = false)
    private BigDecimal rUpperDeviation;
    /**
     * 底R下偏差(mm)
     */
    @TableField(exist = false)
    private BigDecimal rFloorDeviation;
}