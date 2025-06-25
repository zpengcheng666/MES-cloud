package com.miyu.module.tms.controller.admin.toolconfig.vo;

import com.miyu.module.tms.dal.dataobject.fitconfig.FitConfigDO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.*;
import java.math.BigDecimal;

@Schema(description = "管理后台 - 刀具类型新增/修改 Request VO")
@Data
public class ToolConfigSaveReqVO {

    @Schema(description = "ID", requiredMode = Schema.RequiredMode.REQUIRED)
    private String id;

    @Schema(description = "物料类型id")
    private String materialConfigId;

    @Schema(description = "物料编号")
    private String materialNumber;

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

    @Schema(description = "刀组状态0停用1正常")
    private Boolean status;

    @Schema(description = "刀具类型")
    private String toolType;

    @Schema(description = "物料类别ID")
    private String materialTypeId;

    @Schema(description = "刀具类码")
    private String materialTypeCode;

    @Schema(description = "物料类别名称")
    private String materialTypeName;


    private List<String> ids;

    // 几何参数
    @Schema(description = "几何参数集合")
    @Valid
    private List<ToolConfigParameter> geoParamList;

    @Schema(description = "切削参数")
    @Valid
    private List<ToolConfigParameter> cutParamList;


    @Schema(description = "参数集合")
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ToolConfigParameter {

        @Schema(description = "刀具类型ID")
        private String toolConfigId;

        @Schema(description = "参数名称")
        @NotEmpty(message = "参数名称不能为空")
        private String name;

        @Schema(description = "参数值")
        @NotEmpty(message = "参数值不能为空")
        private String value;

        @Schema(description = "参数缩写")
        private String abbr;

        @Schema(description = "计量单位")
        private String unit;

        @Schema(description = "排序")
        @NotNull(message = "排序不能为空")
        private Integer sort;

        @Schema(description = "分类 1 几何参数  2 切削参数")
        private Integer type;

    }

    @Schema(description = "刀具适配集合")
    private List<FitConfigDO> fitConfigList;
}
