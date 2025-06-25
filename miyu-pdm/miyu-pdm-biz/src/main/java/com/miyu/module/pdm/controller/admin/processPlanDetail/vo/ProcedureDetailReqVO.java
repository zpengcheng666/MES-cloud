package com.miyu.module.pdm.controller.admin.processPlanDetail.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema(description = "管理后台 - PDM 选中资源 Request VO")
@Data
public class ProcedureDetailReqVO {
    @TableId(type = IdType.ASSIGN_ID)
    @Schema(description = "id", requiredMode = Schema.RequiredMode.REQUIRED, example = "6902")
    private String id;

    @Schema(description = "零件工艺规程版本id")
    private String processVersionId;

    @Schema(description = "工序id")
    private String procedureId;

    @Schema(description = "制造资源类型：1设备 2刀具 3工装")
    private Integer resourcesType;

    @Schema(description = "制造资源类型ID")
    private String resourcesTypeId;

    @Schema(description = "数量")
    private String quantity;
}
