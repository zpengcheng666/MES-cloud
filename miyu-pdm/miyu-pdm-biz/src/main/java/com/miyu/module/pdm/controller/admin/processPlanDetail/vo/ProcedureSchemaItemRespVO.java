package com.miyu.module.pdm.controller.admin.processPlanDetail.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;

@Schema(description = "PDM - 工序检测项目详情 Response VO")
@Data
public class ProcedureSchemaItemRespVO {
    @Schema(description = "id", example = "1")
    private String id;

    @Schema(description = "检验方案id")
    private String inspectionSchemeId;

    @Schema(description = "工艺规程版本id", example = "1")
    private String processVersionId;

    @Schema(description = "工序id",example = "1")
    private String procedureId;

    @Schema(description = "项目检测id", example = "1")
    private String inspectionItemId;

    @Schema(description = "检测顺序",example = "1")
    private Integer number;

    @Schema(description = "判断方式")
    private Integer referenceType;

    @Schema(description = "上限值")
    private BigDecimal maxValue;

    @Schema(description = "下限值",example = "1")
    private BigDecimal minValue;

    @Schema(description = "技术要求" ,example = "1")
    private String content;

    @Schema(description = "判断",example = "1")
    private String judgement;

    @Schema(description = "接收质量限" ,example = "1")
    private String acceptanceQualityLimit;

    @TableField(exist = false)
    private String inspectionSchemeName;

    @TableField(exist = false)
    private String inspectionItemName;
}
