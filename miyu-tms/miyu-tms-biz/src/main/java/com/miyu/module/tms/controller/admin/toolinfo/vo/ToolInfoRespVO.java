package com.miyu.module.tms.controller.admin.toolinfo.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.math.BigDecimal;
import java.util.*;
import java.util.*;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;
import com.alibaba.excel.annotation.*;

@Schema(description = "管理后台 - 刀组信息 Response VO")
@Data
@ExcelIgnoreUnannotated
public class ToolInfoRespVO {

    @Schema(description = "ID", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("ID")
    private String id;

    @Schema(description = "创建时间", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("创建时间")
    private LocalDateTime createTime;

    @Schema(description = "物料id")
    @ExcelProperty("物料id")
    private String materialStockId;

    @Schema(description = "物料类型id（冗余）")
    @ExcelProperty("物料类型id（冗余）")
    private String materialConfigId;

    @Schema(description = "状态(1，可用，2不可用)")
    @ExcelProperty("状态(1，可用，2不可用)")
    private Integer status;

    @Schema(description = "刀具装配任务id", example = "10627")
    @ExcelProperty("刀具装配任务id")
    private String assembleTaskId;


    @Schema(description = "物料编号")
    private String materialNumber;
    private String materialName;
    //工单号
   
    private String orderNumber;
    //目标位置
    private String targetLocation;
   
    private String targetLocationCode;
    //配送截止时间
    private LocalDateTime distributionDeadline;
    //最短加工时长
    private Integer minimumTime;

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

    @Schema(description = "物料条码")
    private String barCode;
    @Schema(description = "物料所在跟库位")
    private String rootLocationCode;
    @Schema(description = "物料所在跟储位托盘")
    private String storageCode;


    @Schema(description = "刀柄信息")
    private List<AssembleRecordVO>  toolHandleList;
    @Schema(description = "刀具信息")
    private List<AssembleRecordVO>  toolHeadList;
    @Schema(description = "配件")
    private List<AssembleRecordVO>  toolAccessoryList;


    // ===================对刀数据
    //刀具直径
    private BigDecimal diameter;
    //刀具总长
    private BigDecimal totalLength;
    //刀具r角
    private BigDecimal rAngle;
    //刀具额定寿命
    private BigDecimal ratedLife;
    //剩余寿命
    private BigDecimal remainLife;

    // ===================动平衡数据
    //平衡质量等级
    private BigDecimal balancingQuality;
    //操作速度
    private Integer serviceSpeed;
    //质量单位[KG]
    private BigDecimal weight;
    //标准值[gmm]
    private BigDecimal staticUnbalance;
    //动平衡转速
    private Integer rpm;
    //质量等级结果
    private Integer balancingQualityReality;
    //允许机床转速
    private Integer maxSpeed;
    //结果[gmm]
    private BigDecimal gmmResult;

}