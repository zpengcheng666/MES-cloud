package com.miyu.module.pdm.controller.admin.processTask.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema(description = "管理后台 - PDM 外键关联 Request VO")
@Data
public class PbomProcessReqVO {

    @TableId(type = IdType.ASSIGN_ID)
    @Schema(description = "id", example = "20041")
    private String id;

    @Schema(description = "项目号", example = "20041")
    private String projectCode;

    @Schema(description = "零部件版本id", example = "20041")
    private String partVersionId;

    @Schema(description = "项目零件目录id", example = "20041")
    private String projPartBomId;

    @Schema(description = "零件工艺规程版本id", example = "20041")
    private String processVersionId;

    @Schema(description = "是否有效", example = "20041")
    private String isValid;
}
