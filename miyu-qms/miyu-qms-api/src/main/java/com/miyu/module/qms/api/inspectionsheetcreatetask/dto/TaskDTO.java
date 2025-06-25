package com.miyu.module.qms.api.inspectionsheetcreatetask.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@Schema(description = "管理后台 - 检验单任务DTO")
@Data
public class TaskDTO {

    @Schema(description = "检验单名称")
    private String sheetName;

    @Schema(description = "检验单号", requiredMode = Schema.RequiredMode.REQUIRED, example = "赵六")
    private String sheetNo;

    @Schema(description = "来源单号", requiredMode = Schema.RequiredMode.REQUIRED, example = "赵六")
    @NotEmpty(message = "检验单号不能为空")
    private String recordNumber;

    @Schema(description = "工序任务ID")
    private String  recordId;

    // 1 采购收货	2 外协收获	3 外协原材料退货	4 委托加工收货	5 销售退货	6 生产
    @Schema(description = "检验单来源 ")
    private Integer sourceType;

    @Schema(description = "物料类型", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @NotEmpty(message = "物料类型不能为空")
    private String materialConfigId;

    @Schema(description = "物料批次号 ")
    private String batchNumber;

    @Schema(description = "方案类型 1来料检验  2生产检验 3成品检验", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @NotNull(message = "方案类型不能为空")
    private Integer schemeType;

    @Schema(description = "工艺ID", example = "12830")
    private String technologyId;

    @Schema(description = "工序ID", example = "12830")
    private String processId;

    @Schema(description = "检验单任务详情列表")
    @Valid
    @NotEmpty(message = "检验单任务详情不能为空")
    private List<Detail> details;

    @Schema(description = "检验单任务详情")
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Detail {

        @Schema(description = "物料主键", requiredMode = Schema.RequiredMode.REQUIRED, example = "20529")
        @NotEmpty(message = "物料主键不能为空")
        private String materialId;

        @Schema(description = "物料条码", requiredMode = Schema.RequiredMode.REQUIRED, example = "20529")
        @NotEmpty(message = "物料条码不能为空")
        private String barCode;

        @Schema(description = "物料批次号")
        private String batchNumber;
    }
}
