package com.miyu.module.qms.controller.admin.inspectionsheetgeneratetask.vo;

import com.miyu.module.qms.api.inspectionsheetcreatetask.dto.TaskDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import javax.validation.Valid;
import javax.validation.constraints.*;

@Schema(description = "管理后台 - 检验单新增/修改 Request VO")
@Data
public class InspectionSheetGenerateTaskSaveReqVO {

    @Schema(description = "主键", requiredMode = Schema.RequiredMode.REQUIRED, example = "14484")
    private String id;

    @Schema(description = "检验单名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "赵六")
    // @NotEmpty(message = "检验单名称不能为空")
    private String sheetName;

    @Schema(description = "检验单号")
    private String sheetNo;

    @Schema(description = "来源单号")
    private String recordNumber;

    @Schema(description = "工序任务ID")
    private String  recordId;

    @Schema(description = "质检状态  0待派工1待检验2检验中3检验完成", example = "1")
    private Integer status;

    @Schema(description = "检验单来源  	1采购收货	2外协收获	3外协原材料退货	4委托加工收货	5销售退货	6生产", example = "1")
    private Integer sourceType;

    @Schema(description = "物料类型", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @NotEmpty(message = "物料类型不能为空")
    private String materialConfigId;

    @Schema(description = "物料批次号 ")
    private String batchNumber;

    @Schema(description = "方案类型 1来料检验  2生产检验 3成品检验")
    private Integer schemeType;

    @Schema(description = "工艺ID", example = "12830")
    private String technologyId;

    @Schema(description = "工序ID", example = "12830")
    private String processId;

    @Schema(description = "检验单任务详情列表")
    @Valid
    @NotEmpty(message = "检验单任务详情不能为空")
    private List<TaskDTO.Detail> details;

    @Schema(description = "检验单任务详情")
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Detail {

        @Schema(description = "库存主键", requiredMode = Schema.RequiredMode.REQUIRED, example = "20529")
        @NotEmpty(message = "库存主键不能为空")
        private String materialId;

        @Schema(description = "物料条码", requiredMode = Schema.RequiredMode.REQUIRED, example = "20529")
        @NotEmpty(message = "物料条码不能为空")
        private String barCode;

        @Schema(description = "物料批次号")
        private String batchNumber;
    }
}
