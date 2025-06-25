package com.miyu.cloud.mcs.dto.productionProcess;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class McsPlanPartDTO {

    @Schema(description = "工艺规程版本id")
    private String processVersionId;

    @Schema(description = "工艺规程编号")
    private String processCode;

    @Schema(description = "工艺规程名称")
    private String processName;

    @Schema(description = "加工方案码")
    private String processSchemeCode;

    @Schema(description = "工艺规程版次")
    private String processVersion;

    @Schema(description = "零件图号")
    private String partNumber;

    @Schema(description = "零件名称")
    private String partName;

    @Schema(description = "零件版次")
    private String partVersion;

    @Schema(description = "毛坯外形尺寸")
    private String singleSize;

    @Schema(description = "成组加工尺寸")
    private String groupSize;

    @Schema(description = "工作说明")
    private String description;

    @Schema(description = "材料ID")
    private String materialId;

    @Schema(description = "材料编号")
    private String materialNumber;

    @Schema(description = "材料牌号")
    private String materialDesg;

    @Schema(description = "材料名称")
    private String materialName;

    @Schema(description = "材料规格")
    private String materialSpecification;
}
