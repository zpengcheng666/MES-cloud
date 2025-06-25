package com.miyu.module.pdm.controller.admin.processPlanDetail.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema(description = "管理后台 - PDM 数控程序 Request VO")
@Data
public class NcReqVO {
    @TableId(type = IdType.ASSIGN_ID)
    @Schema(description = "id", requiredMode = Schema.RequiredMode.REQUIRED, example = "6902")
    private String id;

    @Schema(description = "数控程序id")
    private String ncId;

    @Schema(description = "数控程序名称")
    private String ncName;

    @Schema(description = "数控程序地址")
    private String ncUrl;

}
