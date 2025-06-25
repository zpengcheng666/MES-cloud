package com.miyu.module.pdm.controller.admin.processPlanDetail.vo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema(description = "管理后台 - 工序草图 Response VO")
@Data
@ExcelIgnoreUnannotated
public class ProcedureFileRespVO {

    @TableId(type = IdType.ASSIGN_ID)
    @Schema(description = "ID")
    private String id;

    @Schema(description = "工艺规程版本id")
    private String processVersionId;

    @Schema(description = "工序id")
    private String procedureId;

    @Schema(description = "草图编号")
    private String sketchCode;

    @Schema(description = "草图地址")
    private String sketchUrl;
}
