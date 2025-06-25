package com.miyu.module.tms.controller.admin.toolconfig.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.miyu.module.tms.dal.dataobject.fitconfig.FitConfigDO;
import com.miyu.module.tms.dal.dataobject.toolconfigparameter.ToolConfigParameterDO;
import com.miyu.module.tms.dal.dataobject.toolparamtemplatedetail.ToolParamTemplateDetailDO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import com.alibaba.excel.annotation.*;

@Schema(description = "管理后台 - 刀具类型 Response VO")
@Data
@ExcelIgnoreUnannotated
public class ToolConfigRespVO {

    @Schema(description = "ID", requiredMode = Schema.RequiredMode.REQUIRED)
    private String id;

    @Schema(description = "物料类型id")
    private String materialConfigId;

    @Schema(description = "物料类型名称", example = "12577")
    private String materialConfigName;

    @Schema(description = "物料编号")
    @ExcelProperty(value = "物料编号", order = 2)
    private String materialNumber;

    @Schema(description = "物料类别ID", example = "12577")
    private String materialTypeId;

    @Schema(description = "物料类码", example = "12577")
    @ExcelProperty(value = "物料类码", order = 4)
    private String materialTypeCode;

    @Schema(description = "物料类别名称", example = "12577")
    @ExcelProperty(value = "物料类别", order = 3)
    private String materialTypeName;

    @Schema(description = "名称")
    @ExcelProperty(value = "物料名称", order = 1)
    private String toolName;

    @Schema(description = "型号")
    @ExcelProperty("型号")
    private String toolModel;

    @Schema(description = "重量")
    @ExcelProperty("重量")
    private Double toolWeight;

    @Schema(description = "材质")
    @ExcelProperty("材质")
    private String toolTexture;

    @Schema(description = "涂层")
    @ExcelProperty("涂层")
    private String toolCoating;

    @Schema(description = "额定寿命")
    @ExcelProperty("额定寿命")
    private BigDecimal ratedLife;

    @Schema(description = "最高转速(mm)")
    @ExcelProperty("最高转速(mm)")
    private Integer maxSpeed;

    @Schema(description = "总长上限(mm)")
    @ExcelProperty("总长上限(mm)")
    private BigDecimal lengthUpper;

    @Schema(description = "总长下限(mm)")
    @ExcelProperty("总长下限(mm)")
    private BigDecimal lengthFloor;

    @Schema(description = "玄长上限(mm)")
    @ExcelProperty("玄长上限(mm)")
    private BigDecimal hangingLengthUpper;

    @Schema(description = "玄长下限(mm)")
    @ExcelProperty("玄长下限(mm)")
    private BigDecimal hangingLengthFloor;

    @Schema(description = "刃径上偏差(mm)")
    @ExcelProperty("刃径上偏差(mm)")
    private BigDecimal bladeUpperDeviation;

    @Schema(description = "刃径下偏差(mm)")
    @ExcelProperty("刃径下偏差(mm)")
    private BigDecimal bladeFloorDeviation;

    @Schema(description = "底R上偏差(mm)")
    @ExcelProperty("底R上偏差(mm)")
    @JsonProperty
    private BigDecimal rUpperDeviation;

    @Schema(description = "底R下偏差(mm)")
    @ExcelProperty("底R下偏差(mm)")
    @JsonProperty
    private BigDecimal rFloorDeviation;

    @Schema(description = "刀组状态0停用1正常")
    private Integer status;

    @Schema(description = "创建时间", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("创建时间")
    private LocalDateTime createTime;


    @Schema(description = "规格")
    @ExcelProperty("规格")
    private String spec;


    private List<ToolParamTemplateDetailDO> geoParamList;

    private List<ToolParamTemplateDetailDO> cutParamList;

    private List<ToolConfigParameterDO> templateParamList;


    @Schema(description = "刀具适配集合")
    private List<FitConfigDO> fitConfigList;
}
