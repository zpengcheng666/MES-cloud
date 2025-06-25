package com.miyu.module.pdm.controller.admin.processPlanDetail.vo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Set;

@Schema(description = "管理后台 - 工步信息 Response VO")
@Data
@ExcelIgnoreUnannotated
public class StepRespVO {

    @Schema(description = "工步id")
    private String id;

    @Schema(description = "工步序号")
    private String stepNum;

    @Schema(description = "工步名称")
    private String stepName;

    @Schema(description = "工步关重属性")
    private Integer stepProperty;

    @Schema(description = "工作说明")
    private String description;

    @Schema(description = "工作说明-预览", example = "20041")
    private String descriptionPreview;

    @Schema(description = "工时(min)")
    private Integer processingTime;

    @Schema(description = "工步分类编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "11161")
    private Long categoryId;

    @Schema(description = "槽号-荧光线", example = "1")
    private Set<Long> slotNumber;

    @Schema(description = "装炉温度(℃)-热处理或荧光线")
    private String furnaceTemperature;

    @Schema(description = "加热温度(℃)-热处理或荧光线")
    private String heatTemperature;

    @Schema(description = "升温时间(min)-热处理或荧光线")
    private String heatUpTime;

    @Schema(description = "保温时间(min)-热处理或荧光线")
    private String keepHeatTime;

    @Schema(description = "冷却介质-热处理或荧光线")
    private String coolingMedium;

    @Schema(description = "冷却温度(℃)-热处理或荧光线")
    private String coolingTemperature;

    @Schema(description = "冷却时间(s)-热处理或荧光线")
    private String coolingTime;
}
