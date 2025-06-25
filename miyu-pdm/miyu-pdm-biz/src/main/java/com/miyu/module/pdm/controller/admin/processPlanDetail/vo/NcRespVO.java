package com.miyu.module.pdm.controller.admin.processPlanDetail.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;


@Schema(description = "管理后台 - PDM 数控程序 Response VO")
@Data
public class NcRespVO {
    @TableId(type = IdType.ASSIGN_ID)
    @Schema(description = "id", requiredMode = Schema.RequiredMode.REQUIRED, example = "6902")
    private String id;

    @Schema(description = "零件工艺规程版本id")
    private String processVersionId;

    @Schema(description = "工步id")
    private String stepId;

    @Schema(description = "数控程序id")
    private String ncId;

    @Schema(description = "工步id")
    private String ncName;

    @Schema(description = "数控程序id")
    private String ncUrl;
}
