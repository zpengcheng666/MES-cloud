package com.miyu.module.qms.controller.admin.inspectionsheetgeneratetask.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.time.LocalDateTime;
import com.alibaba.excel.annotation.*;

@Schema(description = "管理后台 - 检验单 Response VO")
@Data
@ExcelIgnoreUnannotated
public class InspectionSheetGenerateTaskRespVO {

    @Schema(description = "主键", requiredMode = Schema.RequiredMode.REQUIRED, example = "14484")
    @ExcelProperty("主键")
    private String id;

    @Schema(description = "创建时间", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("创建时间")
    private LocalDateTime createTime;

    @Schema(description = "检验单名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "赵六")
    @ExcelProperty("检验单名称")
    private String sheetName;

    @Schema(description = "检验单号")
    @ExcelProperty("检验单号")
    private String sheetNo;

    @Schema(description = "来源单号")
    @ExcelProperty("来源单号")
    private String recordNumber;

    @Schema(description = "质检状态  0待派工1待检验2检验中3检验完成", example = "1")
    @ExcelProperty("质检状态  0待派工1待检验2检验中3检验完成")
    private Integer status;

    @Schema(description = "检验单来源  	1采购收货	2外协收获	3外协原材料退货	4委托加工收货	5销售退货	6生产", example = "1")
    @ExcelProperty("检验单来源  	1采购收货	2外协收获	3外协原材料退货	4委托加工收货	5销售退货	6生产")
    private Integer sourceType;

    @Schema(description = "物料类型ID", example = "12577")
    private String materialConfigId;

    @Schema(description = "物料类型名称", example = "12577")
    private String materialConfigName;

    @Schema(description = "物料批次号")
    private String batchNumber;

    @Schema(description = "产品数量")
    private Integer quantity;

    @Schema(description = "主键")
    private String taskId;

    @Schema(description = "物料编号")
    private String materialNumber;

    @Schema(description = "方案类型 来料检验  过程检验 完工检验", example = "1")
    private Integer schemeType;

    @Schema(description = "工艺ID", example = "8843")
    private String technologyId;

    @Schema(description = "工序ID", example = "25350")
    private String processId;
}
