package com.miyu.module.tms.controller.admin.toolgroup.vo;

import com.miyu.module.tms.dal.dataobject.toolgroupdetail.ToolGroupDetailDO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import java.time.LocalDateTime;
import com.alibaba.excel.annotation.*;

@Schema(description = "管理后台 - 刀具组装 Response VO")
@Data
@ExcelIgnoreUnannotated
public class ToolGroupRespVO {

    @Schema(description = "ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "28298")
    @ExcelProperty("ID")
    private String id;

    @Schema(description = "创建时间", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("创建时间")
    private LocalDateTime createTime;

    @Schema(description = "成品刀具类型id", example = "12938")
    @ExcelProperty("成品刀具类型id")
    private String mainConfigId;

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

    @Schema(description = "刀柄")
    private List<ToolGroupDetailDO> handle;

    @Schema(description = "刀具")
    private List<ToolGroupDetailDO> tool;

    @Schema(description = "配件")
    private List<ToolGroupDetailDO> accessories;

}
