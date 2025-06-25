package com.miyu.module.qms.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.List;

@Schema(description = "RPC 服务 - 质量系统 检验方案 Response DTO")
@Data
public class InspectionSchemeRespDTO {


    /**
     * 主键
     */
    private String id;
    /**
     * 方案名称
     */
    private String schemeName;
    /**
     * 方案编号
     */
    private String schemeNo;
    /**
     * 方案类型 来料检验  过程检验 完工检验
     *
     *
     */
    private Integer schemeType;
    /**
     * 物料类型ID
     */
    private String materialConfigId;
    /**
     * 检验级别
     */
    private Integer inspectionLevel;
    /**
     * 是否生效
     */
    private Integer isEffective;
    /**
     * 物料编号
     */
    private String materialNumber;
    /**
     * 物料类码
     */
    private String materialCode;
    /**
     * 物料名称
     */
    private String materialName;
    /**
     * 物料属性（成品、毛坯、辅助材料）
     */
    private Integer materialProperty;
    /**
     * 物料类型（零件、托盘、工装、夹具、刀具）
     */
    private Integer materialType;
    /**
     * 物料规格
     */
    private String materialSpecification;
    /**
     * 物料品牌
     */
    private String materialBrand;
    /**
     * 物料单位
     */
    private String materialUnit;
    /**
     * 工艺ID
     */
    private String technologyId;
    /**
     * 工序ID
     */
    private String processId;
    /***
     * 抽样标准
     */
    private String samplingStandardId;

    /***
     * 抽样准则
     */
    private Integer samplingLimitType;
    /**
     * 接收质量限（AQL）
     */
    private BigDecimal acceptanceQualityLimit;

    /**
     * 检验类型1抽检2全检
     */
    private Integer inspectionSheetType;

    /**
     * 检验水平类型
     */
    private Integer inspectionLevelType;
    /**
     * 类型  1正常检查2加严检查3放宽检查
     */
    private Integer samplingRuleType;


    @Schema(description = "检测项目集合")
    private List<InspectionSchemeRespDTO.Item> items;

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

        @Schema(description = "检测项名称")
        private String inspectionItemName;

    }

}
