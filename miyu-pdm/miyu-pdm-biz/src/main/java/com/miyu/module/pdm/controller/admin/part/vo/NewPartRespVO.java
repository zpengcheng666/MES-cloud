package com.miyu.module.pdm.controller.admin.part.vo;


import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema(description = "管理后台 - 产品数据对象 Response VO")
@Data
@ExcelIgnoreUnannotated
public class NewPartRespVO {

    @Schema(description = "id", requiredMode = Schema.RequiredMode.REQUIRED, example = "6902")
    private String id;

    @Schema(description = "类型(0根节点1厂家2零件)")
    private Integer type;

    @Schema(description = "上级节点")
    private String parentId;

    @Schema(description = "节点名称")
    private String name;

    @Schema(description = "序列号")
    private String serialnum;

    @Schema(description = "客户化标识")
    private String customizedIndex;

    @Schema(description = "零部件版本ID")
    private String partVersionId;

    @Schema(description = "工艺规程版本id")
    private String processVersionId;

    @Schema(description = "零件图号")
    private String partNumber;

    @Schema(description = "产品根节点ID", example = "24352")
    private String rootproductId;

    @Schema(description = "产品目录Id")
    private String datapackageBomId;

}
