package com.miyu.cloud.mcs.dto.externalManufacture;

import com.miyu.cloud.mcs.dto.productionProcess.ProcedureFileRespDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Data
public class McsExternalSSPLPlanDTO {

    //物料唯一码
    @Schema(description = "物料编码集合", example = "[BC-1234,BC-4567]")
    private List<String> materialCode;
    //物料编任务
    @Schema(description = "物料编任务")
    private List<McsMaterialPlanDTO> materialPlan;
    //批次编码(订单编码)
    @Schema(description = "批次编码", example = "OR123")
    private String orderNumber;
    //加工JSON程序
    @Schema(description = "加工JSON程序")
    private String processingJson;
    //加工JSON程序
    @Schema(description = "工装类型编码")
    private String toolTypeNumber;

    //工序序号
    @Schema(description = "工序序号", example = "15")
    private String processNumber;
    //工序名称
    @Schema(description = "工序名称", example = "加工")
    private String processName;
    //工步编号
    @Schema(description = "工步编号", example = "15.01")
    private String stepNumber;
    //工步名称
    @Schema(description = "工步名称", example = "加工装夹")
    private String stepName;
    //设备编码
    @Schema(description = "设备编码", example = "abc")
    private String deviceNumber;

    @Schema(description = "工艺规程编号")
    private String processCode;

    @Schema(description = "工艺规程名称")
    private String processSpecificationName;

    @Schema(description = "加工方案码")
    private String processSchemeCode;

    @Schema(description = "工艺规程版次")
    private String processVersion;

    @Schema(description = "零件图号")
    private String partNumber;

    @Schema(description = "零件名称")
    private String partName;

    @Schema(description = "工作说明")
    private String description;

    @Schema(description = "工作说明-预览(html)")
    private String descriptionPreview;

    @Schema(description = "工序草图列表")
    private List<ProcedureFileRespDTO> fileList;

    @Schema(description = "毛坯外形尺寸")
    private String singleSize;

    @Schema(description = "成组加工尺寸")
    private String groupSize;

    @Schema(description = "材料编号")
    private String materialNumber;

    @Schema(description = "材料规格")
    private String materialSpecification;

    @Schema(description = "工装名称")
    private String toolName;

    @Schema(description = "工装规格")
    private String toolSpecification;

}
