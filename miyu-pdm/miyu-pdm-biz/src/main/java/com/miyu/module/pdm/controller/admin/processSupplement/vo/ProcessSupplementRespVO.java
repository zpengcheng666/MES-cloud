package com.miyu.module.pdm.controller.admin.processSupplement.vo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Schema(description = "pdm - 补加工工艺规程信息 Response VO")
@Data
@ExcelIgnoreUnannotated
public class ProcessSupplementRespVO {
    @Schema(description = "补加工工艺规程ID")
    private String id;

    @Schema(description = "项目号")
    private String projectCode;

    @Schema(description = "零件图号")
    private String partNumber;

    @Schema(description = "零件名称")
    private String partName;

    @Schema(description = "加工状态")
    private String processCondition;

    @Schema(description = "工艺规程版本Id")
    private String processVersionId;

    @Schema(description = "工艺规程编号")
    private String processCode;

    @Schema(description = "当前工序id")
    private String procedureId;

    @Schema(description = "当前工序号")
    private String procedureNum;

    @Schema(description = "补加工后工序id")
    private String procedureIdNext;

    @Schema(description = "补加工后工序号")
    private String procedureNumNext;

    @Schema(description = "补加工工艺规程编号")
    private String processCodeSupplement;

    @Schema(description = "工作说明")
    private String description;

    @Schema(description = "状态")
    private String status;

    @Schema(description = "流程实例的编号")
    private String processInstanceId;

}
