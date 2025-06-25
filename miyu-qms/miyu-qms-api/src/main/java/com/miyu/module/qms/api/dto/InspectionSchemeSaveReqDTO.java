package com.miyu.module.qms.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.List;

@Schema(description = "管理后台 - 检验方案新增/修改 Request VO")
@Data
public class InspectionSchemeSaveReqDTO {

    @Schema(description = "主键")
    private String id;

    @Schema(description = "方案名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "芋艿")
    @NotEmpty(message = "方案名称不能为空")
    private String schemeName;

    @Schema(description = "方案编号", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotEmpty(message = "方案编号不能为空")
    private String schemeNo;

    @Schema(description = "方案类型 来料检验  过程检验 完工检验", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @NotNull(message = "方案类型不能为空")
    private Integer schemeType;

    @Schema(description = "物料类型ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "13208")
    @NotEmpty(message = "物料类型不能为空")
    private String materialConfigId;

    @Schema(description = "检验级别")
    private Integer inspectionLevel = 1;

    @Schema(description = "是否生效", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer isEffective;

    @Schema(description = "物料编号")
    @NotEmpty(message = "物料编号不能为空")
    private String materialNumber;

    @Schema(description = "物料类码")
    private String materialCode;

    @Schema(description = "物料名称", example = "赵六")
    private String materialName;

    @Schema(description = "物料属性（成品、毛坯、辅助材料）")
    private Integer materialProperty;

    @Schema(description = "物料类型（零件、托盘、工装、夹具、刀具）", example = "2")
    private Integer materialType;

    @Schema(description = "物料规格")
    private String materialSpecification;

    @Schema(description = "物料品牌")
    private String materialBrand;

    @Schema(description = "物料单位")
    private String materialUnit;

    @Schema(description = "工艺ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "1234")
    @NotEmpty(message = "工艺不能为空")
    private String technologyId;

    @Schema(description = "工序ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "12830")
    @NotEmpty(message = "工艺不能为空")
    private String processId;

    @Schema(description = "抽样规则ID", example = "5316")
    private String samplingStandardId = "1807613792217788417";

    @Schema(description = "抽样准则", example = "5316")
    private Integer samplingLimitType = 1;

    @Schema(description = "接收质量限")
    private BigDecimal acceptanceQualityLimit = new BigDecimal(1);

    @Schema(description = "检验类型")
    private Integer inspectionSheetType = 2;


    @Schema(description = "检验水平")
    private Integer inspectionLevelType = 5;

    @Schema(description = "抽样类型")
    private Integer samplingRuleType = 1;

    @Schema(description = "是否检验")
    private String isInspect;


    @Schema(description = "检测项目集合")
    @Valid
    @NotEmpty(message = "检测项目不能为空")
    private List<InspectionSchemeSaveReqDTO.Item> items;

    @Schema(description = "检测项目集合")
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Item {

        @Schema(description = "主键")
        private String id;

        @Schema(description = "方案ID")
        private String inspectionSchemeId;

        @Schema(description = "检测项目ID")
        @NotEmpty(message = "检测项目不能为空")
        private String inspectionItemId;

        @Schema(description = "检测顺序")
        @NotNull(message = "检测顺序不能为空")
        private Integer number;

        @Schema(description = "判断方式")
        private Integer referenceType;

        @Schema(description = "上限值")
        private BigDecimal maxValue;

        @Schema(description = "下限值")
        private BigDecimal minValue;

        @Schema(description = "技术要求")
        private String content;

    }
}
