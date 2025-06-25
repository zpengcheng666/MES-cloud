package com.miyu.module.pdm.controller.admin.processTask.vo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Set;

@Schema(description = "管理后台 - PDM 工序 Request VO")
@Data
@ExcelIgnoreUnannotated
public class ProcedureRespVO {
    @TableId(type = IdType.ASSIGN_ID)
    @Schema(description = "id", example = "20041")
    private String id;

    @Schema(description = "工艺规程版本id", example = "20041")
    private String processVersionId;

    @Schema(description = "工序序号", example = "20041")
    private String procedureNum;

    @Schema(description = "工序名称(加工路线)", example = "20041")
    private String procedureName;

    @Schema(description = "是否检验", example = "20041")
    private Integer isInspect;

    @Schema(description = "工序关重属性", example = "20041")
    private Integer procedureProperty;

    @Schema(description = "是否外委", example = "20041")
    private Integer isOut;

    @Schema(description = "准备工时", example = "20041")
    private Integer preparationTime;

    @Schema(description = "加工工时", example = "20041")
    private Integer processingTime;

    @Schema(description = "工作说明", example = "20041")
    private String description;

    @Schema(description = "工作说明-预览", example = "20041")
    private String descriptionPreview;
}
