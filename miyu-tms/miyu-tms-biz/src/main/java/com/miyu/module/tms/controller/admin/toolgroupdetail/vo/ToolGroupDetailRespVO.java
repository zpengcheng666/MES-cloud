package com.miyu.module.tms.controller.admin.toolgroupdetail.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.time.LocalDateTime;
import com.alibaba.excel.annotation.*;

@Schema(description = "管理后台 - 刀具组装 Response VO")
@Data
@ExcelIgnoreUnannotated
public class ToolGroupDetailRespVO {

    @Schema(description = "ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "29203")
    @ExcelProperty("ID")
    private String id;

    @Schema(description = "创建时间", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("创建时间")
    private LocalDateTime createTime;

    @Schema(description = "成品刀具类型id", example = "25360")
    @ExcelProperty("成品刀具类型id")
    private String mainConfigId;

    @Schema(description = "刀位（非必填）")
    @ExcelProperty("刀位（非必填）")
    private Integer site;

    @Schema(description = "装配刀具类型id", example = "9521")
    @ExcelProperty("装配刀具类型id")
    private String accessoryConfigId;

    @Schema(description = "数量(仅限配件使用)", example = "15317")
    @ExcelProperty("数量(仅限配件使用)")
    private Integer count;

    @Schema(description = "物料编号")
    private String materialNumber;

    @Schema(description = "物料名称")
    private String materialName;

    @Schema(description = "物料类码")
    private String materialCode;

    @Schema(description = "物料类别")
    private String materialType;

    @Schema(description = "型号")
    private String toolModel;

    @Schema(description = "重量")
    private Double toolWeight;

    @Schema(description = "材质")
    private String toolTexture;

    @Schema(description = "涂层")
    private String toolCoating;


}
