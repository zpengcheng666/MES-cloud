package com.miyu.module.pdm.controller.admin.processTask.vo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema(description = "管理后台 - 工艺技术编制 Response VO")
@Data
@ExcelIgnoreUnannotated
public class ProcessDetailRespVO {
    @TableId(type = IdType.ASSIGN_ID)
    @Schema(description = "id", requiredMode = Schema.RequiredMode.REQUIRED, example = "6902")
    private String id;

    @Schema(description = "项目号")
    private String projectCode;

    @Schema(description = "零件版本ID")
    private String partVersionId;

    @Schema(description = "零件工艺规程版本id")
    private String processVersionId;

    @Schema(description = "制造资源类型：1设备 2刀具 3工装")
    private Integer resourcesType;

    @Schema(description = "制造资源类型ID")
    private String resourcesTypeId;

    @Schema(description = "数量")
    private String quantity;
}
