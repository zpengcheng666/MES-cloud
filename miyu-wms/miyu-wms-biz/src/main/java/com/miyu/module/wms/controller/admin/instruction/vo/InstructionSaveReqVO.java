package com.miyu.module.wms.controller.admin.instruction.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import javax.validation.constraints.*;

@Schema(description = "管理后台 - 指令新增/修改 Request VO")
@Data
public class InstructionSaveReqVO {

    @Schema(description = "ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "7277")
    private String id;

    @Schema(description = "指令编码")
    private String insCode;

    @Schema(description = "物料库存id", example = "28973")
    private String materialStockId;

    @Schema(description = "指令类型（上架指令、下架指令）", example = "2")
    private Integer insType;

    @Schema(description = "指令状态（未开始、进行中、已完成、已取消）", example = "1")
    private Integer insStatus;

    @Schema(description = "起始库位id", example = "29629")
    private String startLocationId;

    @Schema(description = "目标库位id", example = "14052")
    private String targetLocationId;

    @Schema(description = "指令内容")
    private String insContent;

    @Schema(description = "指令描述", example = "你猜")
    private String insDescription;

}