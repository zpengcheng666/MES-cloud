package com.miyu.module.qms.controller.admin.inspectionsheetgeneratetaskdetail.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.time.LocalDateTime;
import com.alibaba.excel.annotation.*;

@Schema(description = "管理后台 - 检验单生成任务明细 Response VO")
@Data
@ExcelIgnoreUnannotated
public class InspectionSheetGenerateTaskDetailRespVO {

    @Schema(description = "主键", requiredMode = Schema.RequiredMode.REQUIRED, example = "18519")
    @ExcelProperty("主键")
    private String id;

    @Schema(description = "创建时间", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("创建时间")
    private LocalDateTime createTime;

    @Schema(description = "检验单任务ID", example = "2297")
    @ExcelProperty("检验单任务ID")
    private String taskId;

    @Schema(description = "物料ID", example = "14286")
    @ExcelProperty("物料ID")
    private String materialId;

    @Schema(description = "物料条码")
    @ExcelProperty("物料条码")
    private String barCode;

    @Schema(description = "物料批次号")
    private String batchNumber;


}
