package com.miyu.module.pdm.controller.admin.structureDefinition.vo;

import com.miyu.module.pdm.dal.dataobject.structure.StructureExcelDO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@Schema(description = "管理后台 - PDM 数据包结构新增/修改 Request VO")
@Data
public class StructureSaveReqVO {
    @Schema(description = "结构编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "5860")
    private Long id;

    @Schema(description = "父结构编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "21829")
    private Long parentId;

    @Schema(description = "节点类型", requiredMode = Schema.RequiredMode.REQUIRED, example = "10")
    private Integer type;

    @Schema(description = "节点名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "芋艿")
    @NotEmpty(message = "节点名称不能为空")
    private String name;

    @Schema(description = "压缩包方式", requiredMode = Schema.RequiredMode.REQUIRED, example = "zip")
    private String compressType;

    @Schema(description = "设计单位", requiredMode = Schema.RequiredMode.REQUIRED, example = "test")
    private String designUnit;

    @Schema(description = "开启状态", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @NotNull(message = "开启状态不能为空")
    private Integer status;

    @Schema(description = "文件类型", requiredMode = Schema.RequiredMode.REQUIRED, example = "0")
    private Integer fileType;

    @Schema(description = "文件夹路径", requiredMode = Schema.RequiredMode.REQUIRED, example = "0")
    private String dirPath;

    @Schema(description = "绝对路径", requiredMode = Schema.RequiredMode.REQUIRED, example = "0")
    private String absolutePath;

    @Schema(description = "excel列表起始行", requiredMode = Schema.RequiredMode.REQUIRED, example = "0")
    private Integer startRow;

    @Schema(description = "excel规则列表")
    private List<StructureExcelDO> structureExcels;

}
