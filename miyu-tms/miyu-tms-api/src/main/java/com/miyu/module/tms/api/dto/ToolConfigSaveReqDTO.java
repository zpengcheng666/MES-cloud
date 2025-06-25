package com.miyu.module.tms.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import java.math.BigDecimal;

@Schema(description = "管理后台 - 刀具类型新增/修改 Request VO")
@Data
public class ToolConfigSaveReqDTO {

    @Schema(description = "物料类型id")
    private String materialConfigId;

    @Schema(description = "物料编号")
    private String materialNumber;

    @Schema(description = "刀具类码")
    private String materialTypeCode;

    @Schema(description = "刀具类型")
    private String toolType;

    @Schema(description = "物料名称")
    private String materialName;

    @Schema(description = "名称")
    private String toolName;

    @Schema(description = "型号")
    private String toolModel;

    @Schema(description = "重量")
    private Double toolWeight;

    @Schema(description = "材质")
    private String toolTexture;

    @Schema(description = "涂层")
    private String toolCoating;

    @Schema(description = "额定寿命")
    private BigDecimal ratedLife;


    @Schema(description = "最高转速(mm)")
    private Integer maxSpeed;

    @Schema(description = "总长上限(mm)")
    private BigDecimal lengthUpper;

    @Schema(description = "总长下限(mm)")
    private BigDecimal lengthFloor;

    @Schema(description = "玄长上限(mm)")
    private BigDecimal hangingLengthUpper;

    @Schema(description = "玄长下限(mm)")
    private BigDecimal hangingLengthFloor;

    @Schema(description = "刃径上偏差(mm)")
    private BigDecimal bladeUpperDeviation;

    @Schema(description = "刃径下偏差(mm)")
    private BigDecimal bladeFloorDeviation;

    @Schema(description = "底R上偏差(mm)")
    private BigDecimal rUpperDeviation;

    @Schema(description = "底R下偏差(mm)")
    private BigDecimal rFloorDeviation;

    /**
     * 审批状态
     */
    private Integer status;

    /**
     * 工作流编号
     *
     * 关联 ProcessInstance 的 id 属性
     */
    private String processInstanceId;
}
